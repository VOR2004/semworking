package ru.itis.semworkapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.service.product.ProductService;
import ru.itis.semworkapp.service.storage.ImageStorageService;
import ru.itis.semworkapp.service.user.UserService;
import ru.itis.semworkapp.service.user.impl.UserProfileServiceImpl;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final UserProfileServiceImpl userProfileService;
    private final ProductService productService;
    private final ImageStorageService imageStorageService;

    @GetMapping("/account")
    public String userProducts(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        UserEntity user = userService.requireUserByEmail(userDetails.getUsername());
        model.addAttribute("products", productService.getByUser(user));
        model.addAttribute("userProfile", userProfileService.getUserProfileById(user.getId()));
        return "account/products";
    }

    @PostMapping("/account/update/avatar")
    public String updateAvatarUrl(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        UserEntity user = userService.requireUserByEmail(userDetails.getUsername());
        String avatarUrl = imageStorageService.uploadFile(avatarFile);
        userProfileService.updateAvatarUrl(user.getId(), avatarUrl);
        redirectAttributes.addFlashAttribute("message", "Аватар обновлен");
        return "redirect:/account";
    }

    @PostMapping("/account/update/phoneNumber")
    public String updatePhoneNumber(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String phoneNumber,
            RedirectAttributes redirectAttributes
    ) {
        UserEntity user = userService.requireUserByEmail(userDetails.getUsername());
        userProfileService.updatePhoneNumber(user.getId(), phoneNumber);
        redirectAttributes.addFlashAttribute("message", "Телефон обновлен");
        return "redirect:/account";
    }

    @PostMapping("/account/update/bio")
    public String updateBio(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String bio,
            RedirectAttributes redirectAttributes
    ) {
        UserEntity user = userService.requireUserByEmail(userDetails.getUsername());
        userProfileService.updateBio(user.getId(), bio);
        redirectAttributes.addFlashAttribute("message", "Информация о себе обновлена");
        return "redirect:/account";
    }
}
