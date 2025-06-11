package ru.itis.semworkapp.mappers;

import org.springframework.stereotype.Component;
import ru.itis.semworkapp.dto.TagDto;
import ru.itis.semworkapp.entities.TagEntity;

@Component
public class TagMapper {
    public TagDto toDto(TagEntity entity) {
        return TagDto.builder()
                .name(entity.getName())
                .id(entity.getId())
                .build();
    }

    public TagEntity toEntity(TagDto dto) {
        return TagEntity.builder()
                .name(dto.getName())
                .id(dto.getId())
                .build();
    }
}


