package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final Validation validation;
    private final UserStorage userDbStorage;
    private long userId = 0;

    @Autowired
    public UserService(Validation validation, UserStorage userDbStorage) {
        this.validation = validation;
        this.userDbStorage = userDbStorage;
    }

    public User create(User user) {
        log.debug("Получен запрос POST /users.");
        validateUser(user);
        userId++;
        user.setId(userId);
        return userDbStorage.create(user);
    }

    public void validateUser(User user) {
        validation.validateUser(user);
    }

    public User update(User user) {
        log.debug("Получен запрос PUT /users.");
        validateUser(user);
        if (userDbStorage.getUserById(user.getId()).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        return userDbStorage.update(user);
    }

    public void delete(long id) {
        log.debug("Получен запрос DELETE /users/{id}.");
        userDbStorage.delete(id);
    }


    public Collection<User> findAll() {
        log.debug("Получен запрос GET /users.");
        return userDbStorage.getAllUsers();
    }

    public void addFriend(long id, long friendId) {
        log.debug("Получен запрос PUT /users/{id}/friends/{friendId}.");
        if (userDbStorage.getUserById(id).isEmpty() || userDbStorage.getUserById(friendId).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        userDbStorage.addFriend(id, friendId);
        log.debug("Пользователи c id {} и id {} теперь друзья", id,
                friendId);
    }

    public Optional<User> getUser(Long id) {
        log.debug("Получен запрос GET /users/{id}");
        if (userDbStorage.getUserById(id).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        return userDbStorage.getUserById(id);
    }

    public List<User> getFriendsList(long id) {
        log.debug("Получен запрос GET /users/{id}/friends");
        if (userDbStorage.getUserById(id).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        log.debug("Получен список друзей пользователя c id {}", id);
        return userDbStorage.getFriendsList(id);
    }

    public void deleteFriend(long id, long friendId) {
        log.debug("Получен запрос DELETE /users/{id}/friends/{friendId}.");
        if (userDbStorage.getUserById(id).isEmpty() || userDbStorage.getUserById(friendId).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        userDbStorage.deleteFriend(id, friendId);
        log.debug("Пользователи c id {} и id {} теперь не друзья", id,
                friendId);
    }
    public List<User> getCommonFriend(Long id, Long otherId) {
        if (userDbStorage.getUserById(id).isEmpty() || userDbStorage.getUserById(otherId).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        List<User> firstFriendsList = userDbStorage.getFriendsList(id);
        List<User> secondFriendsList = userDbStorage.getFriendsList(otherId);
        log.debug("Получен список общих друзей пользователей id {} и id {}", id,
                otherId);
        if (firstFriendsList.isEmpty() || secondFriendsList.isEmpty()) {
            return new ArrayList<>();
        }
        return firstFriendsList.stream().filter(secondFriendsList::contains)
                .collect(Collectors.toList());
    }
}


