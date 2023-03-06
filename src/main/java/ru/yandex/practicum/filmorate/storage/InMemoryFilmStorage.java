package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static int generatorId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        film.setId(++generatorId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        int id = film.getId();
        if(!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм не найден.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> findFilmById(int id) {
        return Optional.ofNullable(films.get(id));
    }
}
