package ru.itis.semworkapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.semworkapp.service.product.ProductService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;

    @GetMapping("/delete-product")
    public String deleteProductPage(Model model) {
        model.addAttribute("error", null);
        return "admin/delete-product";
    }

    @PostMapping("/delete-product")
    public String deleteProduct(@RequestParam("productId") String productId, Model model) {
        boolean deleted = false;
        try {
            deleted = productService.deleteProductById(productId);
        } catch (Exception ignored) {}
        if (!deleted) {
            model.addAttribute("error", "Товар не найден или не удалён");
        } else {
            model.addAttribute("error", null);
        }
        return "admin/delete-product";
    }
}
