package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAllMpa() {
        String sql = "SELECT * FROM mpa_rating";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Optional<Mpa> findMpaById(int id) {
        String sql = "SELECT * FROM mpa_rating where rating_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), id).stream().findFirst();
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        int id = rs.getInt("rating_id");
        String name = rs.getString("name");
        return new Mpa(id, name);
    }
}
