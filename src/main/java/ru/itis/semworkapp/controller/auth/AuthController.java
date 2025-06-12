package ru.itis.semworkapp.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.semworkapp.forms.RegistrationForm;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "auth/register";
    }

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "message", required = false) String message,
            Model model) {
        if (error != null) {
            model.addAttribute("formError", message);
        }
        return "auth/login";
    }
}

