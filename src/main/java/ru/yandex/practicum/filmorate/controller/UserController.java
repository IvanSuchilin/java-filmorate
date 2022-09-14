package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidBirthdateException;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.InvalidLoginException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users =  new HashMap<>();
    private int userId = 0;

    @GetMapping
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        userId++;
        user.setId(userId);
        validateUser(user);
        users.put(user.getId(), user);
        log.debug("Создан новый пользователь с именем: {}", user.getName());
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        validateUser(user);
        if (!users.containsKey(user.getId())){
            throw new RuntimeException("Нет такого id");
        }
        users.put(user.getId(), user);
        log.debug("Обновлены данные пользователя с именем: {}", user.getName());
        return user;

    }

    private void validateUser (User user){
        if(user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Адрес электронной почты не может быть пустым и должен содержать символ @");
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым и должен содержать символ @");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new InvalidBirthdateException("Дата рождения не может быть в будущем");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")){
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new InvalidLoginException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName().isBlank() || user.getName() == null){
            log.error("Имя пользователя путо - используется логин в качестве имени");
            user.setName(user.getLogin());
        }
    }
}
