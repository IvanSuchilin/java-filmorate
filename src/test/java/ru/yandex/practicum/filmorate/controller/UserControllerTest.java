package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.InvalidBirthdateException;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.InvalidLoginException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserControllerTest {
    UserController userController = new UserController();

    @Test
    void userWithWrongEmailTest() {
        User user = User.builder()
                .id(1)
                .email("mail")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1990,11,14))
                .build();
        InvalidEmailException thrown = Assertions.assertThrows(InvalidEmailException.class,
                () -> userController.validateUser(user));
        Assertions.assertEquals("Адрес электронной почты не может быть пустым и " +
                "должен содержать символ @", thrown.getMessage());
    }

    @Test
    void userWithEmptyEmailTest() {
        User user = User.builder()
                .id(1)
                .email("")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1990,11,14))
                .build();
        InvalidEmailException thrown = Assertions.assertThrows(InvalidEmailException.class,
                () -> userController.validateUser(user));
        Assertions.assertEquals("Адрес электронной почты не может быть пустым " +
                "и должен содержать символ @", thrown.getMessage());
    }

    @Test
    void userWithWrongLoginTest() {
        User user = User.builder()
                .id(1)
                .email("@mail")
                .login("log in")
                .name("name")
                .birthday(LocalDate.of(1990,11,14))
                .build();
        InvalidLoginException thrown = Assertions.assertThrows(InvalidLoginException.class,
                () -> userController.validateUser(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", thrown.getMessage());
    }

    @Test
    void userWithEmptyLoginTest() {
        User user = User.builder()
                .id(1)
                .email("@mail")
                .login("")
                .name("name")
                .birthday(LocalDate.of(1990,11,14))
                .build();
        InvalidLoginException thrown = Assertions.assertThrows(InvalidLoginException.class,
                () -> userController.validateUser(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", thrown.getMessage());
    }

    @Test
    void userWithWrongDateTest() {
        User user = User.builder()
                .id(1)
                .email("@mail")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(3000,11,14))
                .build();
        InvalidBirthdateException thrown = Assertions.assertThrows(InvalidBirthdateException.class,
                () -> userController.validateUser(user));
        Assertions.assertEquals("Дата рождения не может быть в будущем", thrown.getMessage());
    }

    @Test
    void userWithEmptyNameTest() {
        User user = User.builder()
                .id(1)
                .email("@mail")
                .login("login")
                .name("")
                .birthday(LocalDate.of(1990,11,14))
                .build();
        userController.validateUser(user);
        assertEquals("login", user.getName());
    }
}
