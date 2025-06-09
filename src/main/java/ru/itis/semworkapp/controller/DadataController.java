package ru.itis.semworkapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itis.semworkapp.service.api.DadataService;

@RestController
@RequestMapping("/api/dadata")
@RequiredArgsConstructor
public class DadataController {
    private final DadataService dadataService;

    @GetMapping("/suggest")
    public String suggest(@RequestParam String query) {
        return dadataService.suggestAddress(query);
    }
}
