package ru.itis.semworkapp.exceptions.auth;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email уже существует");
    }
}