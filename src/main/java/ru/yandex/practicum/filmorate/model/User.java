package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;


@Data
@Builder
public class User {
    private Integer id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    private LocalDate birthday;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        User user = (User) o;
//        return Objects.equals(email, user.email) &&
//               Objects.equals(login, user.login);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(email, login);
//    }
}
