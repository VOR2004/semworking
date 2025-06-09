package ru.itis.semworkapp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.itis.semworkapp.entities.ProductEntity;

import java.util.List;

public interface ProductRepositoryCustom {
    Page<ProductEntity> searchByTitleDescriptionOrTags(String query, List<String> tagsNames, Pageable pageable);
}