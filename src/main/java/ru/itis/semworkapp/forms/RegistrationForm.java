package ru.itis.semworkapp.forms;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Форма для регистрации")
public class RegistrationForm {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 3, max = 30, message = "Имя пользователя должно быть от 3 до 30 символов")
    private String username;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Неверный формат email")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен содержать не менее 6 символов")
    private String password;

    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    private String confirmPassword;
}
