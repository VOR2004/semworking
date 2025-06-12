package ru.itis.semworkapp.exceptions.chat;

public class ChatInvalidIdException extends RuntimeException {
    public ChatInvalidIdException() {
        super("Chat not found: try another id");
    }
}
