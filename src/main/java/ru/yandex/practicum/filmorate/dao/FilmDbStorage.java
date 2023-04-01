package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Film> findAllFilms() {
        String sql = "SELECT f.film_id, f.name, f.description, f.releaseDate, f.duration, " +
                "mpa.rating_id, mpa.name AS mpa_name " +
                "FROM films AS f " +
                "INNER JOIN mpa_rating AS mpa ON mpa.rating_id = f.rating_id " +
                "ORDER BY f.film_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Optional<Film> findFilmById(int id) {
        String sql = "SELECT f.film_id, f.name, f.description, f.releaseDate, f.duration, " +
                "mpa.rating_id, mpa.name AS mpa_name " +
                "FROM films AS f " +
                "INNER JOIN mpa_rating AS mpa ON mpa.rating_id = f.rating_id " +
                "WHERE f.film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id).stream().findFirst();

    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("film_id");
        Film film = Film.builder()
                .id(id)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Mpa(rs.getInt("rating_id"), rs.getString("mpa_name")))
                .build();
        film.setGenres(genreStorage.findGenresByFilm(id));
        Set<Integer> likes = film.getLikes();
        likes.addAll(jdbcTemplate.query("SELECT user_id FROM Likes WHERE film_id = ?",
                (rz, rowNum) -> rz.getInt("user_id"), id));
        return film;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO films (name, description, releaseDate, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        Mpa mpa = mpaStorage.findMpaById(film.getMpa().getId()).orElseThrow(() -> new MpaNotFoundException("Рейтинг MPA не найден."));
        film.setMpa(mpa);
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where name = ?", film.getName());
        if (filmRows.next()) {
            film.setId(filmRows.getInt("film_id"));
        }
        updateGenres(film.getGenres(), film.getId());
        film.setGenres(genreStorage.findGenresByFilm(film.getId()));
        return film;
    }

    @Override
    public Film update(Film film) {
        int id = film.getId();
        if (!findFilmById(id).isPresent()) {
            throw new FilmNotFoundException("Фильм не найден.");
        }
        String sql = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, rating_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), id);
        updateGenres(film.getGenres(), film.getId());
        film.setGenres(genreStorage.findGenresByFilm(film.getId()));
        return film;
    }

    private void updateGenres(List<Genre> genres, int id) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", id);
        if (genres != null) {
            String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            genres.forEach(genre -> jdbcTemplate.update(sql, id, genre.getId()));
        }
    }

    @Override
    public void addLike(int id, int userId) {
        removeLike(id, userId);
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public void removeLike(int id, int userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, id, userId);
    }
}
