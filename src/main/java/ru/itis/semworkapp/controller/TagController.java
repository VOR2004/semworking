package ru.itis.semworkapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.semworkapp.dto.TagDto;
import ru.itis.semworkapp.service.tag.TagService;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/search")
    public List<String> searchTags(@RequestParam String query) {
        return tagService.searchTags(query)
                .stream()
                .map(TagDto::getName)
                .toList();
    }
}
