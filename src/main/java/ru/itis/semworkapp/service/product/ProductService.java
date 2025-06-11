package ru.itis.semworkapp.service.product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.itis.semworkapp.dto.ProductDto;
import ru.itis.semworkapp.entities.ProductEntity;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.forms.ProductForm;
import ru.itis.semworkapp.forms.VoteForm;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductDto> getAllProducts();
    void saveProduct(ProductForm form, UserEntity user) throws IOException;

    ProductForm getProductFormForEdit(UUID productId, UserEntity user);

    List<String> getImageUrls(UUID productId, UserEntity user);

    void updateProduct(UUID productId, ProductForm form, UserEntity user) throws IOException;

    String getMainImageUrl(UUID productId, UserEntity user);

    void voteProduct(VoteForm voteForm);

    void update(ProductEntity product);
    void delete(ProductEntity product);
    List<ProductDto> getByUser(UserEntity user);

    ProductEntity requireProductById(UUID id);

    ProductDto getProductById(UUID id);

    Page<ProductDto> getAllProducts(Pageable pageable);

    Page<ProductDto> searchProducts(String query, List<String> tags, Pageable pageable);

    boolean userOwnsProduct(UserEntity user, ProductEntity product);
    List<ProductDto> findByTag(String tagName);
    public boolean deleteProductById(String id);
}
