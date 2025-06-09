package ru.itis.semworkapp.service.tag.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.semworkapp.entities.TagEntity;
import ru.itis.semworkapp.repositories.TagRepository;
import ru.itis.semworkapp.service.tag.TagService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public TagEntity findOrCreateByName(String name) {
        return tagRepository.findByNameIgnoreCase(name.trim())
                .orElseGet(() -> tagRepository.save(TagEntity.builder().name(name.trim()).build()));
    }
    @Override
    public List<TagEntity> findAllTags() {
        return tagRepository.findAll();
    }
    @Override
    public List<TagEntity> searchTags(String query) {
        return tagRepository.findByNameContainingIgnoreCase(query);
    }
}