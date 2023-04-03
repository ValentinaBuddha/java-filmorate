package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final FilmService filmService;

    @GetMapping
    public List<Genre> getGenres() {
        log.info("GET / '/genres'");
        return filmService.findGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable("id") int id) {
        log.info("GET / '/genres/{id}'", id);
        return filmService.getGenreById(id);
    }
}
