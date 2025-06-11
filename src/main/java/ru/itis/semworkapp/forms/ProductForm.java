package ru.itis.semworkapp.forms;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductForm {
    @NotBlank(message = "Название обязательно")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    private String title;

    @NotBlank(message = "Описание обязательно")
    @Size(max = 2000, message = "Описание не должно превышать 2000 символов")
    private String description;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.01", message = "Минимальная цена — 0.01")
    private BigDecimal price;

    @Size(max = 3, message = "Максимум 3 тега")
    private List<@NotBlank(message = "Тег не может быть пустым") String> tagNames;

    @NotBlank(message = "Адрес обязателен")
    private String address;

    @NotBlank(message = "Широта обязательна")
    private String lat;

    @NotBlank(message = "Долгота обязательна")
    private String lon;

    private Integer mainImageIndex;

    @Size(max = 10, message = "Максимум 10 изображений")
    private List<MultipartFile> images;

    private List<String> existingImageUrls;
}
