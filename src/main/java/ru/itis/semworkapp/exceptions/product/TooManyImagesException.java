package ru.itis.semworkapp.exceptions.product;

public class TooManyImagesException extends RuntimeException {
    public TooManyImagesException() {
        super("Можно загрузить не более 10 изображений");
    }
}