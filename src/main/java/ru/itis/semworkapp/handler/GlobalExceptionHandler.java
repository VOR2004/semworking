package ru.itis.semworkapp.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.itis.semworkapp.exceptions.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ChatNotFoundException.class)
    public String handleChatNotFound(ChatNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(ChatAccessDeniedException.class)
    public String handleAccessDenied(ChatAccessDeniedException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/403";
    }
}
