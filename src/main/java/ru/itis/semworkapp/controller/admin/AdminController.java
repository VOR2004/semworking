package ru.itis.semworkapp.controller.admin;

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
    public String deleteProductPage() {
        return "admin/delete-product";
    }

    @PostMapping("/delete-product")
    public String deleteProduct(@RequestParam("productId") String productId, Model model) {
        boolean deleted = false;
        try {
            deleted = productService.deleteProductById(productId);
        } catch (Exception ignored) {}
        if (!deleted) {
            model.addAttribute("error", AdminMessages.PRODUCT_DELETED_OR_NOT_FOUND);
        } else {
            model.addAttribute("error", AdminMessages.PRODUCT_DELETED);
        }
        return "admin/delete-product";
    }
}
