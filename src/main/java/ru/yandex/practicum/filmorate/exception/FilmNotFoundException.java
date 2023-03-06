package ru.yandex.practicum.filmorate.exception;

import java.util.function.Supplier;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(String message) {
        super(message);
    }
}