package ru.itis.semworkapp.exceptions.auth;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }
}