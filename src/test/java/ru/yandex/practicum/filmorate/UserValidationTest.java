package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {
    private UserController userController;
    private User user;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void validateUserEmailEmpty() {
        user = User.builder()
                .id(1)
                .email("")
                .login("ya")
                .name("Ivan")
                .birthday(LocalDate.of(1986, 11, 15))
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    void validateUserEmailIncorrect() {
        user = User.builder()
                .id(1)
                .email("123ya.ru")
                .login("ya")
                .name("Ivan")
                .birthday(LocalDate.of(1986, 11, 15))
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    void validateUserLoginEmpty() {
        user = User.builder()
                .id(1)
                .email("123@ya.ru")
                .login("")
                .name("Ivan")
                .birthday(LocalDate.of(1986, 11, 15))
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    void validateUserLoginSpaceMiddle() {
        user = User.builder()
                .id(1)
                .email("123@ya.ru")
                .login("y a")
                .name("Ivan")
                .birthday(LocalDate.of(1986, 11, 15))
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    void validateUserLoginSpaceLeft() {
        user = User.builder()
                .id(1)
                .email("123@ya.ru")
                .login(" ya")
                .name("Ivan")
                .birthday(LocalDate.of(1986, 11, 15))
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    void validateUserLoginSpaceRight() {
        user = User.builder()
                .id(1)
                .email("123@ya.ru")
                .login("ya ")
                .name("Ivan")
                .birthday(LocalDate.of(1986, 11, 15))
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    void validateUserNameEmpty() {
        user = User.builder()
                .id(1)
                .email("123@ya.ru")
                .login("ya")
                .name("")
                .birthday(LocalDate.of(1986, 11, 15))
                .build();
        userController.validateUser(user);
        assertEquals("ya", user.getName(), "Валидация по пустому имени не сработала.");
    }

    @Test
    void validateUserBirthday() {
        user = User.builder()
                .id(1)
                .email("123@ya.ru")
                .login("ya")
                .name("Ivan")
                .birthday(LocalDate.MAX)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }
}