package ru.itis.semworkapp.exceptions;

public class ChatInvalidIdException extends RuntimeException {
    public ChatInvalidIdException() {
        super("Chat not found: try another id");
    }
}
