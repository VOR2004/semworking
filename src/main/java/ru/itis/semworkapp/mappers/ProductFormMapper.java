package ru.itis.semworkapp.mappers;

import org.springframework.stereotype.Component;
import ru.itis.semworkapp.entities.ProductEntity;
import ru.itis.semworkapp.entities.TagEntity;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.forms.ProductForm;

@Component
public class ProductFormMapper {
    public ProductForm toForm(ProductEntity product) {
        ProductForm form = new ProductForm();
        form.setTitle(product.getTitle());
        form.setDescription(product.getDescription());
        form.setPrice(product.getPrice());
        form.setLat(product.getLat());
        form.setLon(product.getLon());
        form.setTagNames(product.getTags().stream().map(TagEntity::getName).toList());
        return form;
    }

    public ProductEntity toEntity(ProductForm form, UserEntity user) {
        return ProductEntity.builder()
                .title(form.getTitle())
                .description(form.getDescription())
                .price(form.getPrice())
                .userEntity(user)
                .lon(form.getLon())
                .lat(form.getLat())
                .build();
    }
}
