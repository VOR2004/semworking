package ru.itis.semworkapp.exceptions.auth;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Пароли не совпадают");
    }
}