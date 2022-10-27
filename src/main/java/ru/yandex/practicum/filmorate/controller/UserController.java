package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public Optional<User> get(@PathVariable("id") Long id) {
        log.info("Получение пользователя id {}", id);
        return userService.getUser(id);
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.info("Создание пользователя");
        return userService.create(user);
    }

    @PutMapping("/users")
    public User put(@RequestBody User user) {
        log.info("Получени евсех пользователей");
        return userService.update(user);
    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Удаление пользователя id {}", id);
        userService.delete(id);
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.info("Получение всех пользователей");
        return userService.findAll();
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.info("Добавление в друзья к пользователю {} от пользователя {}", friendId, id);
        userService.addFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriendsList(@PathVariable("id") Long id) {
        log.info("Получение списка друзей пользователя {}", id);
        return userService.getFriendsList(id);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.info("Удаление из друзей  пользователя {} от пользователя {}", friendId, id);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsList(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {
        log.info("Получение общих друзей пользователя {} и пользователя {}", id, otherId);
        return userService.getCommonFriend(id, otherId);
    }
}
