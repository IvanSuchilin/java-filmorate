package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    Validation validation = new Validation();
    private int userId = 0;

    @GetMapping
    public List<User> findAll() {
        log.debug("Получен запрос GET /users.");
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.debug("Получен запрос POST /users.");
        userId++;
        user.setId(userId);
        validation.validateUser(user);
        users.put(user.getId(), user);
        log.debug("Создан новый пользователь с именем: {}", user.getName());
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        log.debug("Получен запрос PUT /users.");
        validation.validateUser(user);
        if (!users.containsKey(user.getId())) {
            throw new RuntimeException("Нет такого id");
        }
        users.put(user.getId(), user);
        log.debug("Обновлены данные пользователя с именем: {}", user.getName());
        return user;
    }
}
