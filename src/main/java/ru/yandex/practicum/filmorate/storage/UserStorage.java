package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAll();
    User create(User user);
    User update(User user);
    Optional<User> findUserById(int id);
}
