package ru.itis.semworkapp.exceptions.product;

public class TooManyTagsException extends RuntimeException {
    public TooManyTagsException() {
        super("Можно выбрать не более 3 тегов");
    }
}