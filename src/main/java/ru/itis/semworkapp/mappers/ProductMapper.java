package ru.itis.semworkapp.mappers;

import org.springframework.stereotype.Component;
import ru.itis.semworkapp.dto.ProductDto;
import ru.itis.semworkapp.entities.ProductEntity;
import ru.itis.semworkapp.entities.TagEntity;

import java.util.stream.Collectors;
@Component
public class ProductMapper {
    public ProductDto toDto(ProductEntity entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .createdAt(entity.getCreatedAt())
                .mainImageUrl(entity.getMainImageUrl())
                .lat(entity.getLat())
                .lon(entity.getLon())
                .rating(entity.getRating())
                .imageUrls(entity.getImageUrls())
                .votedUserIds(entity.getVotedUserIds())
                .userId(entity.getUserEntity().getId())
                .tags(entity.getTags().stream().map(TagEntity::getName).collect(Collectors.toSet()))
                .userEntity(entity.getUserEntity())
                .build();
    }
}