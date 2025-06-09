package ru.itis.semworkapp.repositories;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.semworkapp.entities.ProductEntity;
import ru.itis.semworkapp.entities.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, ProductRepositoryCustom {
    List<ProductEntity> findAllByUserEntity(UserEntity user);
    Optional<ProductEntity> getProductById(UUID id);
    @Query("SELECT p FROM ProductEntity p JOIN p.tags t WHERE LOWER(t.name) = LOWER(:tagName)")
    List<ProductEntity> findByTagName(@Param("tagName") String tagName);
    @NonNull
    Page<ProductEntity> findAll(@NonNull Pageable pageable);
}