package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private Integer id;
    @NotBlank(message = "Электронная почта не может быть пустой.")
    @Email(message = "Электронная почта должна содержать символ @.")
    private String email;
    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы.")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;
}
