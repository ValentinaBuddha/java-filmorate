package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.annotation.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Integer id;
    @NotBlank(message = "Введите название фильма.")
    private String name;
    @NotNull
    @Size(max = 200, message = "Слишком длинное описание.")
    private String description;
    @NotNull
    @ReleaseDate(value = "1895-12-28", message = "Введите дату релиза не ранее 28 декабря 1895 года.")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть больше 0.")
    private Integer duration;
}
