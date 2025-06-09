package ru.itis.semworkapp.service.product.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.semworkapp.entities.ProductEntity;
import ru.itis.semworkapp.entities.TagEntity;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.exceptions.ProductNotFoundException;
import ru.itis.semworkapp.forms.ProductForm;
import ru.itis.semworkapp.repositories.ProductRepository;
import ru.itis.semworkapp.service.storage.ImageStorageService;
import ru.itis.semworkapp.service.product.ProductService;
import ru.itis.semworkapp.service.tag.TagService;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final TagService tagService;
    private final ImageStorageService imageStorageService;
    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void saveProduct(ProductForm form, UserEntity user) throws IOException {
        ProductEntity product = ProductEntity.builder()
                .title(form.getTitle())
                .description(form.getDescription())
                .price(form.getPrice())
                .userEntity(user)
                .lon(form.getLon())
                .lat(form.getLat())
                .build();

        Set<TagEntity> tags = new HashSet<>();
        if (form.getTagNames() != null) {
            if (form.getTagNames().size() > 3) {
                throw new IllegalArgumentException("Максимум 3 тега!");
            }
            for (String tagName : form.getTagNames()) {
                tags.add(tagService.findOrCreateByName(tagName));
            }
        }

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

        product.setTags(tags);
        productRepository.save(product);
    }

    @Override
    public ProductForm getProductFormForEdit(UUID productId, UserEntity user) {
        ProductEntity product = requireProductById(productId);
        if (!userOwnsProduct(user, product)) {
            throw new SecurityException("Нет доступа к этому товару");
        }
        ProductForm form = new ProductForm();
        form.setTitle(product.getTitle());
        form.setDescription(product.getDescription());
        form.setPrice(product.getPrice());
        form.setLat(product.getLat());
        form.setLon(product.getLon());
        form.setTagNames(product.getTags().stream().map(TagEntity::getName).toList());
        return form;
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
                tags.add(tagService.findOrCreateByName(tagName));
            }
        }
        product.setTags(tags);

        // Получаем список изображений, которые пользователь хочет сохранить
        List<String> savedImages = form.getExistingImageUrls() != null ? form.getExistingImageUrls() : new ArrayList<>();

        // Загружаем новые изображения и добавляем их URL
        if (form.getImages() != null && !form.getImages().isEmpty() && !form.getImages().get(0).isEmpty()) {
            List<String> newUrls = imageStorageService.uploadFiles(form.getImages());
            savedImages.addAll(newUrls);
        }

        product.setImageUrls(savedImages);

        // Обновляем главное изображение
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
    public void update(ProductEntity product) {
        productRepository.save(product);
    }

    @Override
    public void delete(ProductEntity product) {
        productRepository.delete(product);
    }

    @Override
    public List<ProductEntity> getByUser(UserEntity user) {
        return productRepository.findAllByUserEntity(user);
    }

    @Override
    public ProductEntity requireProductById(UUID id) {
        return productRepository.getProductById(id)
                .orElseThrow(ProductNotFoundException::new);
    }
    @Override
    public Page<ProductEntity> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<ProductEntity> searchProducts(String query, List<String> tags, Pageable pageable) {
        return productRepository.searchByTitleDescriptionOrTags(query, tags, pageable);
    }

    @Override
    public boolean userOwnsProduct(UserEntity user, ProductEntity product) {
        return product.getUserEntity().getId().equals(user.getId());
    }

    @Override
    public List<ProductEntity> findByTag(String tagName) {
        return productRepository.findByTagName(tagName);
    }
}
