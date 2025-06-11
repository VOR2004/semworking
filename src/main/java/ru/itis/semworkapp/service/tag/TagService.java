package ru.itis.semworkapp.service.tag;

import ru.itis.semworkapp.dto.TagDto;
import ru.itis.semworkapp.entities.TagEntity;
import java.util.List;

public interface TagService {
    TagDto findOrCreateByName(String name);
    List<TagDto> searchTags(String query);
    List<TagDto> findAllTags();
}