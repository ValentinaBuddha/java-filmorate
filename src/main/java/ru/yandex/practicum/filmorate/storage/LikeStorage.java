package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {
    void addLike(int id, int userId);

    void removeLike(int id, int userId);
}
