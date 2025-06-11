package ru.itis.semworkapp.service.tag.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.semworkapp.dto.TagDto;
import ru.itis.semworkapp.entities.TagEntity;
import ru.itis.semworkapp.mappers.TagMapper;
import ru.itis.semworkapp.repositories.TagRepository;
import ru.itis.semworkapp.service.tag.TagService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagDto findOrCreateByName(String name) {
        TagEntity tag = tagRepository.findByNameIgnoreCase(name.trim())
                .orElseGet(() -> tagRepository.save(
                        TagEntity.builder()
                                .name(name.trim())
                                .build()
                ));
        return tagMapper.toDto(tag);
    }
    @Override
    public List<TagDto> findAllTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDto> searchTags(String query) {
        return tagRepository.findByNameContainingIgnoreCase(query).stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());
    }
}