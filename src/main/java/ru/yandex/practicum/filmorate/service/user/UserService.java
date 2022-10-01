package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.ArrayList;
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
        validateUser(user);
        userId++;
        user.setId(userId);
        return userStorage.create(user);
    }

    public User update(User user) {
        Map<Integer, User> actualUsers = userStorage.getUsers();
        if (!actualUsers.containsKey(user.getId())) {
            throw new DataNotFoundException("Нет такого id");
        }
        validateUser(user);
        log.debug("Получен запрос PUT /users.");
        return userStorage.update(user);
    }

    public void delete(Integer id) {
        log.debug("Получен запрос DELETE /users/{id}.");
        Map<Integer, User> actualUsers = userStorage.getUsers();
        if (!actualUsers.containsKey(id)) {
            throw new DataNotFoundException("Нет такого id");
        }
        User user = actualUsers.get(id);
        validateUser(user);
        userStorage.delete(user);

    }

    public void addFriend(int id, int friendId) {
        log.debug("Получен запрос PUT /users/{id}/friends/{friendId}.");
        Map<Integer, User> actualUsers = userStorage.getUsers();
        System.out.println(actualUsers);
        if (!actualUsers.containsKey(id) || !actualUsers.containsKey(friendId)) {
            throw new DataNotFoundException("Нет такого id");
        }
        userStorage.getUsers().get(id).getFriendsId().add(friendId);
        userStorage.getUsers().get(friendId).getFriendsId().add(id);
        log.debug("Пользователи {} и {} теперь друзья", actualUsers.get(id).getName(),
                actualUsers.get(friendId).getName());
    }

    public void deleteFriend(int id, int friendId) {
        log.debug("Получен запрос DELETE /users/{id}/friends/{friendId}.");
        Map<Integer, User> actualUsers = userStorage.getUsers();
        if (!actualUsers.containsKey(id) || !actualUsers.containsKey(friendId)) {
            throw new DataNotFoundException("Нет такого id");
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
            throw new DataNotFoundException("Нет такого id");
        }
        List<Integer> firstFriendsList = actualUsers.get(id).getFriendsId();
        List<Integer> secondFriendsList = actualUsers.get(otherId).getFriendsId();
        log.debug("Получен список общих друзей пользователей {} и {}", actualUsers.get(id).getName(),
                actualUsers.get(otherId).getName());
        if (firstFriendsList.isEmpty() || secondFriendsList.isEmpty()){
            return new ArrayList<>();
        }
        return firstFriendsList.stream().filter(secondFriendsList::contains).collect(Collectors.toList());
    }

    public User getUserById(Integer id) {
        log.debug("Получен запрос GET /users/{id}");
        Map<Integer, User> actualUsers = userStorage.getUsers();
        if (!actualUsers.containsKey(id)) {
            throw new DataNotFoundException("Нет такого id");
        }
        return actualUsers.get(id);
    }

    public List<Integer> getFriendsList(int id){
        log.debug("\"Получен запрос GET /users/{id}/friends");
        Map<Integer, User> actualUsers = userStorage.getUsers();
        log.debug("Получен список друзей пользователя {}", actualUsers.get(id).getName());
        System.out.println(actualUsers.get(id).getFriendsId());
        return actualUsers.get(id).getFriendsId();
    }
}

