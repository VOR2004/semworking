package ru.itis.semworkapp.exceptions;

public class TooManyImagesException extends RuntimeException {
    public TooManyImagesException() {
        super("Можно загрузить не более 10 изображений");
    }
}