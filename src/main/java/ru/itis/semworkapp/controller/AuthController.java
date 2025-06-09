package ru.itis.semworkapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itis.semworkapp.forms.RegistrationForm;
import ru.itis.semworkapp.service.user.UserService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("form")
            @Valid RegistrationForm form,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        userService.registerUser(form);
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
}

