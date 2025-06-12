package ru.itis.semworkapp.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itis.semworkapp.service.api.DadataService;

@Tag(name = "Dadata сontroller", description = "Контроллер для получения адресов Dadata")
@RestController
@RequestMapping("/api/dadata")
@RequiredArgsConstructor
public class DadataController {
    private final DadataService dadataService;

    @Operation(summary = "Получить подсказку по введенным буквам",
            description = "Помогает вывести подсказки при поиске адресов (по совпадениям в сочетаниях букв)")
    @GetMapping("/suggest")
    public String suggest(
            @Parameter(description = "Адрес (может быть неполным), по которому выходят подсказки", required = true)
            @RequestParam String query) {
        return dadataService.suggestAddress(query);
    }
}
