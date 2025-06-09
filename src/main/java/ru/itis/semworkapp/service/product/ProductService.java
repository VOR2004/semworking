package ru.itis.semworkapp.service.product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.itis.semworkapp.entities.ProductEntity;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.forms.ProductForm;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductEntity> getAllProducts();
    void saveProduct(ProductForm form, UserEntity user) throws IOException;

    ProductForm getProductFormForEdit(UUID productId, UserEntity user);

    List<String> getImageUrls(UUID productId, UserEntity user);

    void updateProduct(UUID productId, ProductForm form, UserEntity user) throws IOException;

    String getMainImageUrl(UUID productId, UserEntity user);

    void update(ProductEntity product);
    void delete(ProductEntity product);
    List<ProductEntity> getByUser(UserEntity user);

    ProductEntity requireProductById(UUID id);

    Page<ProductEntity> getAllProducts(Pageable pageable);

    Page<ProductEntity> searchProducts(String query, List<String> tags, Pageable pageable);

    boolean userOwnsProduct(UserEntity user, ProductEntity product);
    List<ProductEntity> findByTag(String tagName);
}
