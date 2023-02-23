package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {
    private static int generatorId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (films.containsValue(film)) {
            throw new ValidationException("Такой фильм уже существует.");
        }
        validateFilm(film);
        film.setId(++generatorId);
        films.put(film.getId(), film);
        log.debug("Фильм под названием {} создан.", film.getName());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        int id = film.getId();
        if(!films.containsKey(id)) {
            log.debug("Фильм не найден.");
            throw new ValidationException("Фильм не найден.");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.debug("Фильм под названием {} обновлен.", film.getName());
        return film;
    }

    public void validateFilm(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Введите название фильма.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Слишком длинное описание.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("Введите дату релиза не ранее 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть больше 0.");
        }
    }
}
