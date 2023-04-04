package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    List<User> findAllFriends(int id);

    List<User> findCommonFriends(int id, int otherId);
}
