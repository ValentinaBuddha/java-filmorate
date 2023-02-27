package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
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
    public Film create(@Valid @RequestBody Film film) {
        film.setId(++generatorId);
        films.put(film.getId(), film);
        log.debug("Фильм под названием {} создан.", film.getName());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        int id = film.getId();
        if(!films.containsKey(id)) {
            log.debug("Фильм не найден.");
            throw new ValidationException("Фильм не найден.");
        }
        films.put(film.getId(), film);
        log.debug("Фильм под названием {} обновлен.", film.getName());
        return film;
    }
}
