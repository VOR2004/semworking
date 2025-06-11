package ru.itis.semworkapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itis.semworkapp.dto.ProductDto;
import ru.itis.semworkapp.entities.ProductEntity;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.exceptions.ImageUploadException;
import ru.itis.semworkapp.exceptions.TooManyImagesException;
import ru.itis.semworkapp.exceptions.TooManyTagsException;
import ru.itis.semworkapp.forms.ProductForm;
import ru.itis.semworkapp.forms.VoteForm;
import ru.itis.semworkapp.service.product.ProductService;
import ru.itis.semworkapp.service.tag.TagService;
import ru.itis.semworkapp.service.user.UserService;

import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final TagService tagService;

    @GetMapping("/")
    public String homePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "tags", required = false) List<String> tags,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ProductDto> productPage;

        if ((query != null && !query.isBlank()) || (tags != null && !tags.isEmpty())) {
            productPage = productService.searchProducts(query, tags, pageable);
        } else {
            productPage = productService.getAllProducts(pageable);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("q", query);
        model.addAttribute("selectedTags", tags);
        model.addAttribute("allTags", tagService.findAllTags());

        return "product/list";
    }

    @PostMapping("/products/vote")
    public String voteProduct(@ModelAttribute VoteForm voteForm,
                              @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.requireUserByEmail(userDetails.getUsername());
        voteForm.setUserId(user.getId());
        productService.voteProduct(voteForm);
        return "redirect:/products/" + voteForm.getProductId();
    }

    @GetMapping("/product/add")
    public String addForm(Model model) {
        model.addAttribute("form", new ProductForm());
        return "product/add";
    }

    @PostMapping("/product/add")
    @ResponseBody
    public ResponseEntity<?> addProduct(
            @Valid @ModelAttribute("form") ProductForm form,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
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
            return ResponseEntity.badRequest().body(Collections.singletonMap("tagNames", e.getMessage()));
        } catch (TooManyImagesException | ImageUploadException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("images", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Неизвестная ошибка: " + e.getMessage()));
        }
    }


    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable UUID id,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        ProductEntity product = productService.requireProductById(id);
        UserEntity user = userService.requireUserByEmail(userDetails.getUsername());
        boolean userVoted = product.getVotedUserIds().contains(user.getId());

        model.addAttribute("product", product);
        model.addAttribute("userVoted", userVoted);
        return "product/view";
    }

    @GetMapping("/product/edit/{id}")
    public String editForm(
            @PathVariable UUID id,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserEntity user = userService.requireUserByEmail(userDetails.getUsername());
        ProductForm form = productService.getProductFormForEdit(id, user);
        model.addAttribute("form", form);
        model.addAttribute("productId", id);
        model.addAttribute("existingImages", productService.getImageUrls(id, user));
        String mainImageUrl = productService.getMainImageUrl(id, user);
        model.addAttribute("mainImageUrl", mainImageUrl);
        return "account/edit";
    }

    @PostMapping("/product/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> editProduct(
            @PathVariable UUID id,
            @ModelAttribute("form") @Valid ProductForm form,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
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
            return ResponseEntity.badRequest().body(Collections.singletonMap("tagNames", e.getMessage()));
        } catch (TooManyImagesException | ImageUploadException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("images", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Неизвестная ошибка: " + e.getMessage()));
        }
    }


    @PostMapping("/product/delete/{id}")
    public String deleteProduct(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ProductEntity product = productService.requireProductById(id);
        UserEntity user = userService.requireUserByEmail(userDetails.getUsername());

        if (!productService.userOwnsProduct(user, product)) {
            return "redirect:/";
        }

        productService.delete(product);
        return "redirect:/account";
    }
}

