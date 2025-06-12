package ru.itis.semworkapp.exceptions.chat;

import java.util.UUID;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(UUID id) {
        super("Chat with id " + id + " not found");
    }
}