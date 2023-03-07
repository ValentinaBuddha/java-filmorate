package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static int generatorId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(++generatorId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        int id = user.getId();
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        users.put(id, user);
        return user;
    }

    @Override
    public Optional<User> findUserById(int id) {
        return Optional.ofNullable(users.get(id));
    }
}
