package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAll();

    User create(User user);

    User update(User user);

    Optional<User> findUserById(int id);

    void addFriend(int userId, int friendId);

    void removeFriend(int id, int friendId);

    List<User> findFriends(int id);

    List <User> findCommonFriends(int id, int otherId);
}