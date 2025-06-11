package ru.itis.semworkapp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.semworkapp.entities.UserEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private String mainImageUrl;
    private String lat;
    private String lon;
    private Integer rating;
    private List<String> imageUrls;
    private Set<UUID> votedUserIds;
    private UUID userId;
    private Set<String> tags;
    private UserEntity userEntity;
}