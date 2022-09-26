package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    //private Map<Integer, User> users = new HashMap<>();
    private Validation validation;
    private UserStorage userStorage;
    private int userId = 0;

    @Autowired
    public UserService (Validation validation, InMemoryUserStorage userStorage){
        this.validation = validation;
        this.userStorage = userStorage;
    }
    public void validateUser(User user){
        validation.validateUser(user);
    }

    public List<User> findAll() {
        log.debug("Получен запрос GET /users.");
        log.debug("Текущее количество пользователей: {}", userStorage.getUsers().size());
        return new ArrayList<>(userStorage.getUsers().values());
    }

    public User create(User user) {
        log.debug("Получен запрос POST /users.");
        userId++;
        user.setId(userId);
        validateUser(user);
        return userStorage.create(user);
    }

    public User put(User user) {
        log.debug("Получен запрос PUT /users.");
        validateUser(user);
        return userStorage.put(user);
    }

    public void delete(Integer id){
        log.debug("Получен запрос DELETE /users/{id}.");
        User user = userStorage.getUsers().get(id);
        validateUser(user);
        userStorage.delete(user);}

    public void addFriend(int id, int friendId) {
        log.debug("Получен запрос PUT /users/{id}/friends/{friendId}.");
        Map<Integer, User> actualUsers = userStorage.getUsers();
        if (!actualUsers.containsKey(id) || !actualUsers.containsKey(friendId)) {
            throw new RuntimeException("Нет такого id");
        }
        actualUsers.get(id).getFriendsId().add(friendId);
        actualUsers.get(friendId).getFriendsId().add(id);
        log.debug("Пользователи {} и {} теперь друзья", actualUsers.get(id).getName(),
                actualUsers.get(friendId).getName());
    }

    public void deleteFriend(int id, int friendId) {
        log.debug("Получен запрос DELETE /users/{id}/friends/{friendId}.");
        Map<Integer, User> actualUsers = userStorage.getUsers();
        if (!actualUsers.containsKey(id) || !actualUsers.containsKey(friendId)) {
            throw new RuntimeException("Нет такого id");
        }
        actualUsers.get(id).getFriendsId().remove(friendId);
        actualUsers.get(friendId).getFriendsId().remove(id);
        log.debug("Пользователи {} и {} теперь не друзья", actualUsers.get(id).getName(),
                actualUsers.get(friendId).getName());
    }

    public List<Integer> getCommonFriend(int id, int otherId) {
        log.debug("Получен запрос GET /users/{id}/friends/common/{otherId}.");
        Map<Integer, User> actualUsers = userStorage.getUsers();
        if (!actualUsers.containsKey(id) || !actualUsers.containsKey(otherId)) {
            throw new RuntimeException("Нет такого id");
        }
        List<Integer> firstFriendsList = actualUsers.get(id).getFriendsId();
        List<Integer> secondFriendsList = actualUsers.get(otherId).getFriendsId();
        log.debug("Получен список общих друзей пользователей {} и {} ", actualUsers.get(id).getName(),
                actualUsers.get(otherId).getName());
        return firstFriendsList.stream().filter(secondFriendsList::contains).collect(Collectors.toList());
    }
}

