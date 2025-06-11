package ru.itis.semworkapp.exceptions;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Пароли не совпадают");
    }
}