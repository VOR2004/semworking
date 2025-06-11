package ru.itis.semworkapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itis.semworkapp.exceptions.EmailAlreadyExistsException;
import ru.itis.semworkapp.exceptions.PasswordMismatchException;
import ru.itis.semworkapp.forms.RegistrationForm;
import ru.itis.semworkapp.service.user.UserService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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
    @ResponseBody
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegistrationForm form,
            BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            userService.registerUser(form);
            return ResponseEntity.ok().body(Collections.singletonMap("success", true));
        } catch (EmailAlreadyExistsException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("email", e.getMessage());
            log.warn("Registration failed: email {} already exists", form.getEmail());
            return ResponseEntity.badRequest().body(errors);
        } catch (PasswordMismatchException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("confirmPassword", e.getMessage());
            log.warn("Registration failed: password mismatch for email {}", form.getEmail());
            return ResponseEntity.badRequest().body(errors);
        }
    }



    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "message", required = false) String message,
                            Model model) {
        if (error != null) {
            model.addAttribute("formError", message);
        }
        return "auth/login";
    }
}

