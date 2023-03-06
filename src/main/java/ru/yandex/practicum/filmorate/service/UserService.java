package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validate(user);
        return userStorage.update(user);
    }

    public User findUserById(int id) {
        return userStorage.findUserById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    public void addFriend(int id, int friendId) {
        if (id < 0 || friendId < 0) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        findUserById(id).getFriends().add(friendId);
        findUserById(friendId).getFriends().add(id);
    }

    public List<User> findAllFriends(int id) {
        List<User> friendsList = new ArrayList<>();
        Set<Integer> friends = findUserById(id).getFriends();
        if (friends == null) {
            return friendsList;
        }
        for (Integer friendId : friends) {
            User friend = findUserById(friendId);
            friendsList.add(friend);
        }
        return friendsList;
    }

    public List<User> findCommonFriends(int id, int otherId) {
        List<User> commonFriends = findAllFriends(id);
        List<User> commonFriendsSecond = findAllFriends(otherId);
        commonFriends.retainAll(commonFriendsSecond);
        return commonFriends;
    }

    public void removeFriend(int id, int friendId) {
        findUserById(id).getFriends().remove(friendId);
        findUserById(friendId).getFriends().remove(id);
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}