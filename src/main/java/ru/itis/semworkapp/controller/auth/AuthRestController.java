package ru.itis.semworkapp.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.semworkapp.exceptions.auth.EmailAlreadyExistsException;
import ru.itis.semworkapp.exceptions.auth.PasswordMismatchException;
import ru.itis.semworkapp.forms.RegistrationForm;
import ru.itis.semworkapp.service.user.UserService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "Auth REST controller", description = "Контроллер для регистрации и отдачи ответа на фронт")
@RestController
@RequiredArgsConstructor
public class AuthRestController {
    private final UserService userService;
    @Operation(summary = "Регистрируем юзера",
            description = "Регистрируется пользователь, если в процессе ошибки -- отдаем инфу фронту")
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(
            @Parameter(description = "Форма, которую подаем для регистрации", required = true)
            @Valid @RequestBody RegistrationForm form,
            @Parameter(description = "Результат регистрации (успешный/с ошибками)", required = true)
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
            log.warn(AuthMessages.REG_FAILED_EMAIL, form.getEmail());
            return ResponseEntity.badRequest().body(errors);
        } catch (PasswordMismatchException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("confirmPassword", e.getMessage());
            log.warn(AuthMessages.REG_FAILED_PASSWORD, form.getEmail());
            return ResponseEntity.badRequest().body(errors);
        }
    }
}
