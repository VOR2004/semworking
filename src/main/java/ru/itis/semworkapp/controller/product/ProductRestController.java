package ru.itis.semworkapp.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.exceptions.product.ImageUploadException;
import ru.itis.semworkapp.exceptions.product.TooManyImagesException;
import ru.itis.semworkapp.exceptions.product.TooManyTagsException;
import ru.itis.semworkapp.forms.ProductForm;
import ru.itis.semworkapp.service.product.ProductService;
import ru.itis.semworkapp.service.user.UserService;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Tag(name = "Product REST controller", description = "Контроллер для добавления/изменения товара, отдача ответа фронту")
@RestController
@RequiredArgsConstructor
public class ProductRestController {
    private final UserService userService;
    private final ProductService productService;

    @Operation(summary = "Добавляем товар",
            description = "Добавляем товар, если в процессе ошибки -- отдаем инфу фронту")
    @PostMapping("/product/add")
    @ResponseBody
    public ResponseEntity<?> addProduct(
            @Valid @ModelAttribute("form") ProductForm form,
            @Parameter(description = "Результат добавления (успешный/с ошибками)", required = true)
            BindingResult result,
            @Parameter(description = "Информация о пользователе", required = true)
            @AuthenticationPrincipal UserDetails userDetails) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            UserEntity user = userService.requireUserByEmail(userDetails.getUsername());
            productService.saveProduct(form, user);
            return ResponseEntity.ok().body(Collections.singletonMap("success", true));
        } catch (TooManyTagsException e) {
            log.warn(ProductMessages.TOO_MANY_TAGS);
            return ResponseEntity.badRequest().body(Collections.singletonMap("tagNames", e.getMessage()));
        } catch (TooManyImagesException | ImageUploadException e) {
            log.warn(ProductMessages.TOO_MANY_IMAGES);
            return ResponseEntity.badRequest().body(Collections.singletonMap("images", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap(
                    "error", ProductMessages.UNKNOWN_ERROR + e.getMessage()));
        }
    }

    @Operation(summary = "Изменяем товар",
            description = "Изменяем товар, если в процессе ошибки -- отдаем инфу фронту")
    @PostMapping("/product/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> editProduct(
            @PathVariable UUID id,
            @Parameter(description = "Форма для изменения товара", required = true)
            @ModelAttribute("form")
            @Valid ProductForm form,
            @Parameter(description = "Результат добавления (успешный/с ошибками)", required = true)
            BindingResult result,
            @Parameter(description = "Информация о пользователе", required = true)
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            UserEntity user = userService.requireUserByEmail(userDetails.getUsername());
            productService.updateProduct(id, form, user);
            return ResponseEntity.ok().body(Collections.singletonMap("success", true));
        } catch (TooManyTagsException e) {
            log.warn(ProductMessages.TOO_MANY_TAGS);
            return ResponseEntity.badRequest().body(Collections.singletonMap("tagNames", e.getMessage()));
        } catch (TooManyImagesException | ImageUploadException e) {
            log.warn(ProductMessages.TOO_MANY_IMAGES);
            return ResponseEntity.badRequest().body(Collections.singletonMap("images", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap(
                    "error", ProductMessages.UNKNOWN_ERROR + e.getMessage()));
        }
    }

}
