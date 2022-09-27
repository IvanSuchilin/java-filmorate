package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User put(@RequestBody User user) {
        return userService.put(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id){
        userService.delete(id);
    }

    @GetMapping("/{id}")
    public void get(@PathVariable("id") Integer id){
        userService.getUserById(id);
    }
     @PutMapping("/{id}/friends/{friendId}")
    public void addFriend (@PathVariable("id") int id, @PathVariable("friendId") int friendId){
        userService.addFriend(id, friendId);
     }

    @DeleteMapping ("/{id}/friends/{friendId}")
    public void deleteFriend (@PathVariable("id") int id, @PathVariable("friendId") int friendId){
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<Integer> getFriendsList(@PathVariable("id") Integer id){
        return userService.getFriendsList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<Integer> getCommonFriendsList(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId){
        return userService.getCommonFriend(id, otherId);
    }
}
