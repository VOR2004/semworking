package ru.itis.semworkapp.service.product.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.semworkapp.dto.ProductDto;
import ru.itis.semworkapp.entities.ProductEntity;
import ru.itis.semworkapp.entities.TagEntity;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.exceptions.product.ImageUploadException;
import ru.itis.semworkapp.exceptions.product.ProductNotFoundException;
import ru.itis.semworkapp.exceptions.product.TooManyImagesException;
import ru.itis.semworkapp.exceptions.product.TooManyTagsException;
import ru.itis.semworkapp.forms.ProductForm;
import ru.itis.semworkapp.forms.VoteForm;
import ru.itis.semworkapp.mappers.ProductFormMapper;
import ru.itis.semworkapp.mappers.ProductMapper;
import ru.itis.semworkapp.mappers.TagMapper;
import ru.itis.semworkapp.repositories.product.ProductRepository;
import ru.itis.semworkapp.service.storage.ImageStorageService;
import ru.itis.semworkapp.service.product.ProductService;
import ru.itis.semworkapp.service.tag.TagService;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final TagService tagService;
    private final ImageStorageService imageStorageService;
    private final ProductMapper productMapper;
    private final TagMapper tagMapper;
    private final ProductFormMapper productFormMapper;
    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveProduct(ProductForm form, UserEntity user) throws IOException {
        if (form.getTagNames() != null && form.getTagNames().size() > 3) {
            throw new TooManyTagsException();
        }
        if (form.getImages() != null && form.getImages().size() > 10) {
            throw new TooManyImagesException();
        }
        ProductEntity product = productFormMapper.toEntity(form, user);

        Set<TagEntity> tags = new HashSet<>();
        if (form.getTagNames() != null) {
            for (String tagName : form.getTagNames()) {
                tags.add(tagMapper.toEntity(tagService.findOrCreateByName(tagName)));
            }
        }

        try {
            if (form.getImages() != null && !form.getImages().isEmpty()) {
                List<String> urls = imageStorageService.uploadFiles(form.getImages());
                product.setImageUrls(urls);

                String mainImageUrl = null;
                if (form.getMainImageIndex() != null
                        && form.getMainImageIndex() >= 0
                        && form.getMainImageIndex() < urls.size()) {
                    mainImageUrl = urls.get(form.getMainImageIndex());
                } else if (!urls.isEmpty()) {
                    mainImageUrl = urls.get(0);
                }
                product.setMainImageUrl(mainImageUrl);
            }
        } catch (IOException ex) {
            throw new ImageUploadException("Ошибка загрузки изображений");
        }

        product.setTags(tags);
        productRepository.save(product);
    }

    @Override
    public ProductForm getProductFormForEdit(UUID productId, UserEntity user) {
        ProductEntity product = requireProductById(productId);
        if (!userOwnsProduct(user, product)) {
            throw new SecurityException("Нет доступа к этому товару");
        }
        return productFormMapper.toForm(product);
    }

    @Override
    public List<String> getImageUrls(UUID productId, UserEntity user) {
        ProductEntity product = requireProductById(productId);
        if (!userOwnsProduct(user, product)) {
            throw new SecurityException("Нет доступа к этому товару");
        }
        return product.getImageUrls();
    }

    @Override
    @Transactional
    public void updateProduct(UUID productId, ProductForm form, UserEntity user) throws IOException {
        ProductEntity product = requireProductById(productId);
        if (!userOwnsProduct(user, product)) {
            throw new SecurityException("Нет доступа к этому товару");
        }

        product.setTitle(form.getTitle());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setLat(form.getLat());
        product.setLon(form.getLon());

        Set<TagEntity> tags = new HashSet<>();
        if (form.getTagNames() != null) {
            if (form.getTagNames().size() > 3) {
                throw new IllegalArgumentException("Максимум 3 тега!");
            }
            for (String tagName : form.getTagNames()) {
                tags.add(tagMapper.toEntity(tagService.findOrCreateByName(tagName)));
            }
        }
        product.setTags(tags);

        List<String> savedImages = form.getExistingImageUrls() != null ? form.getExistingImageUrls() : new ArrayList<>();

        if (form.getImages() != null && !form.getImages().isEmpty() && !form.getImages().get(0).isEmpty()) {
            List<String> newUrls = imageStorageService.uploadFiles(form.getImages());
            savedImages.addAll(newUrls);
        }

        product.setImageUrls(savedImages);

        String mainImageUrl = null;
        if (form.getMainImageIndex() != null
                && form.getMainImageIndex() >= 0
                && form.getMainImageIndex() < savedImages.size()) {
            mainImageUrl = savedImages.get(form.getMainImageIndex());
        } else if (!savedImages.isEmpty()) {
            mainImageUrl = savedImages.get(0);
        }
        product.setMainImageUrl(mainImageUrl);

        productRepository.save(product);
    }

    @Override
    public String getMainImageUrl(UUID productId, UserEntity user) {
        ProductEntity product = requireProductById(productId);
        if (!userOwnsProduct(user, product)) {
            throw new SecurityException("Нет доступа к этому товару");
        }
        return product.getMainImageUrl();
    }

    @Override
    public void voteProduct(VoteForm voteForm) {
        ProductEntity product = productRepository.findById(voteForm.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (product.getVotedUserIds().contains(voteForm.getUserId())) {
            throw new IllegalStateException("User already voted for this product");
        }

        product.setRating(product.getRating() + voteForm.getVoteValue());
        product.getVotedUserIds().add(voteForm.getUserId());
        productRepository.save(product);
    }

    @Override
    public void update(ProductEntity product) {
        productRepository.save(product);
    }

    @Override
    public void delete(ProductEntity product) {
        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> getByUser(UserEntity user) {
        return productRepository.findAllByUserEntity(user).stream().map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductEntity requireProductById(UUID id) {
        return productRepository.getProductById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public ProductDto getProductById(UUID id) {
        return productMapper.toDto(requireProductById(id));
    }
    @Override
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> searchProducts(String query, List<String> tags, Pageable pageable) {
        return productRepository.searchByTitleDescriptionOrTags(query, tags, pageable)
                .map(productMapper::toDto);
    }

    @Override
    public boolean userOwnsProduct(UserEntity user, ProductEntity product) {
        return product.getUserEntity().getId().equals(user.getId());
    }

    @Override
    public List<ProductDto> findByTag(String tagName) {
        return productRepository.findByTagName(tagName).stream().map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteProductById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            if (productRepository.existsById(uuid)) {
                productRepository.deleteById(uuid);
                return true;
            }
        } catch (IllegalArgumentException ignored) {}
        return false;
    }
}
