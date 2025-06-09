package ru.itis.semworkapp.forms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductForm {
    private String title;
    private String description;
    private BigDecimal price;
    private List<String> tagNames;
    private String address;
    private String lat;
    private String lon;
    private Integer mainImageIndex;
    private List<MultipartFile> images;
    private List<String> existingImageUrls;
}

