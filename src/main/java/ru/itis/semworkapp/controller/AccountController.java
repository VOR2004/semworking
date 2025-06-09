package ru.itis.semworkapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.repositories.UserRepository;
import ru.itis.semworkapp.service.product.ProductService;
import ru.itis.semworkapp.service.user.UserService;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final ProductService productService;
    @GetMapping("/account")
    public String userProducts(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserEntity user = userService.requireUserByEmail(userDetails.getUsername());
        model.addAttribute("products", productService.getByUser(user));
        return "account/products";
    }
}
