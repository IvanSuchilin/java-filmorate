package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidBirthdateException;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.InvalidLoginException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users =  new HashMap<>();
    private int userId = 0;

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        user.setId(userId++);
        validateUser(user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        validateUser(user);
        users.put(user.getId(), user);
        return user;
    }

    private void validateUser (User user){
        if(user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым и должен содержать символ @");
        }
        if (user.getBirthdate().isAfter(LocalDate.now())) {
            throw new InvalidBirthdateException("Дата рождения не может быть в будущем");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")){
            throw new InvalidLoginException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName().isBlank() || user.getName() == null){
            user.setName(user.getLogin());
        }
    }
}
