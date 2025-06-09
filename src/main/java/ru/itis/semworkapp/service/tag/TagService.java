package ru.itis.semworkapp.service.tag;

import ru.itis.semworkapp.entities.TagEntity;
import java.util.List;

public interface TagService {
    TagEntity findOrCreateByName(String name);
    List<TagEntity> searchTags(String query);
    List<TagEntity> findAllTags();
}