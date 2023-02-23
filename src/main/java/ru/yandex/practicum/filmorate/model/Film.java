package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Integer id;
    @NonNull
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
}
