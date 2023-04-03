package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_FILMS = "SELECT f.film_id, f.name, f.description, f.releaseDate, f.duration, " +
            "mpa.rating_id, mpa.name AS mpa_name " +
            "FROM films AS f " +
            "INNER JOIN mpa_rating AS mpa ON f.rating_id = mpa.rating_id ";

    @Override
    public List<Film> findAllFilms() {
        String sql = "ORDER BY f.film_id";
        return jdbcTemplate.query(SELECT_FILMS + sql, (rs, rowNum) -> makeFilm(rs));

    }

    @Override
    public Optional<Film> findFilmById(int id) {
        String sql = "WHERE f.film_id = ?";
        return jdbcTemplate.query(SELECT_FILMS + sql, (rs, rowNum) -> makeFilm(rs), id).stream().findFirst();
    }

    @Override
    public List<Film> findPopular(int count) {
        String sql = "LEFT JOIN likes ON f.film_id = likes.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(likes.film_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(SELECT_FILMS + sql, (rs, rowNum) -> makeFilm(rs), count);
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
        film.setGenres(findGenresByFilm(id));
        return film;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO films (name, description, releaseDate, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where name = ?", film.getName());
        if (filmRows.next()) {
            film.setId(filmRows.getInt("film_id"));
        }
        updateGenres(film.getGenres(), film.getId());
        film.setGenres(findGenresByFilm(film.getId()));
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
        film.setGenres(findGenresByFilm(id));
        return film;
    }

    private void updateGenres(LinkedHashSet<Genre> genres, int id) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", id);
        if (genres != null) {
            List<Genre> g = new ArrayList<>(genres);
            String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, id);
                            ps.setInt(2, g.get(i).getId());
                        }

                        public int getBatchSize() {
                            return g.size();
                        }
                    });
        }
    }

    @Override
    public LinkedHashSet<Genre> findGenresByFilm(int id) {
        String sql = "SELECT DISTINCT fg.genre_id, genres.name " +
                "FROM film_genres AS fg " +
                "INNER JOIN genres ON genres.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ? " +
                "ORDER BY fg.genre_id ASC";
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.addAll(jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                rs.getString("name")), id));
        return genres;
    }
}
