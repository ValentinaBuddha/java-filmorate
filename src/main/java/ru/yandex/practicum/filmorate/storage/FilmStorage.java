package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> findAllFilms();

    Film create(Film film);

    Film update(Film film);

    Optional<Film> findFilmById(int id);

    void addLike(int id, int userId);

    void removeLike(int id, int userId);
}
