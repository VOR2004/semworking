package ru.itis.semworkapp.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageForm {
    @NotBlank(message = "Сообщение не может быть пустым")
    @Size(max = 1000, message = "Слишком длинное сообщение")
    private String content;
}