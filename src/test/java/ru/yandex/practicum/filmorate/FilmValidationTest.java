package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void validateFilmName() {
        film = Film.builder()
                .id(1)
                .name("")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(2002, 2, 2))
                .duration(100)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
        assertEquals("Введите название фильма.", exception.getMessage());
    }

    @Test
    void validateFilmDescription201() {
        film = Film.builder()
                .id(1)
                .name("Film")
                .description("Из-под покрова тьмы ночной,\n" +
                        "Из чёрной ямы страшных мук\n" +
                        "Благодарю я всех богов\n" +
                        "За мой непокорённый дух.\n" +
                        "\n" +
                        "И я, попав в тиски беды,\n" +
                        "Не дрогнул и не застонал,\n" +
                        "И под ударами судьбы\n" +
                        "Я ранен был, но не упал.\n" +
                        "\n" +
                        "Т")
                .releaseDate(LocalDate.of(2002, 2, 2))
                .duration(100)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
        assertEquals("Слишком длинное описание.", exception.getMessage());
    }

    @Test
    void validateFilmReleaseDate() {
        film = Film.builder()
                .id(1)
                .name("Film")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(100)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
        assertEquals("Введите дату релиза не ранее 28 декабря 1895 года.", exception.getMessage());
    }

    @Test
    void validateFilmDuration0() {
        film = Film.builder()
                .id(1)
                .name("Film")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(0)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
        assertEquals("Продолжительность фильма должна быть больше 0.", exception.getMessage());
    }

    @Test
    void validateFilmDurationNegative() {
        film = Film.builder()
                .id(1)
                .name("Film")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(-100)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
        assertEquals("Продолжительность фильма должна быть больше 0.", exception.getMessage());
    }
}