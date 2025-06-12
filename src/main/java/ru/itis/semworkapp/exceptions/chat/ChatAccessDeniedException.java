package ru.itis.semworkapp.exceptions.chat;

import java.util.UUID;

public class ChatAccessDeniedException extends RuntimeException {
    public ChatAccessDeniedException(UUID chatId) {
        super("Access denied to chat " + chatId);
    }
}