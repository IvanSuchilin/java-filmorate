package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
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

    private Validation validation;
    private UserStorage userStorage;
    private int userId = 0;

    @Autowired
    public UserService(Validation validation, InMemoryUserStorage userStorage) {
        this.validation = validation;
        this.userStorage = userStorage;
    }

    public void validateUser(User user) {
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
        for (int i = 0; i < actualUsers.get(id).getFriendsId().size(); i++) {
            if (actualUsers.get(id).getFriendsId().get(i) == friendId) {
                actualUsers.get(id).getFriendsId().remove(i);
            }
        }
        for (int i = 0; i < actualUsers.get(friendId).getFriendsId().size(); i++) {
            if (actualUsers.get(friendId).getFriendsId().get(i) == id) {
                actualUsers.get(friendId).getFriendsId().remove(i);
            }
        }
        log.debug("Пользователи {} и {} теперь не друзья", actualUsers.get(id).getName(),
                actualUsers.get(friendId).getName());
    }

    public List<User> getCommonFriend(int id, int otherId) {
        log.debug("Получен запрос GET /users/{id}/friends/common/{otherId}.");
        Map<Integer, User> actualUsers = userStorage.getUsers();
        if (!actualUsers.containsKey(id) || !actualUsers.containsKey(otherId)) {
            throw new DataNotFoundException("Нет такого id");
        }
        List<Integer> firstFriendsList = actualUsers.get(id).getFriendsId();
        List<Integer> secondFriendsList = actualUsers.get(otherId).getFriendsId();
        log.debug("Получен список общих друзей пользователей {} и {}", actualUsers.get(id).getName(),
                actualUsers.get(otherId).getName());
        if (firstFriendsList.isEmpty() || secondFriendsList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> commonIdList = firstFriendsList.stream().filter(secondFriendsList::contains)
                .collect(Collectors.toList());
        return commonIdList.stream().map(actualUsers::get).collect(Collectors.toList());
    }

    public User getUserById(Integer id) {
        log.debug("Получен запрос GET /users/{id}");
        Map<Integer, User> actualUsers = userStorage.getUsers();
        if (!actualUsers.containsKey(id)) {
            throw new DataNotFoundException("Нет такого id");
        }
        return actualUsers.get(id);
    }

    public List<User> getFriendsList(int id) {
        log.debug("\"Получен запрос GET /users/{id}/friends");
        Map<Integer, User> actualUsers = userStorage.getUsers();
        log.debug("Получен список друзей пользователя {}", actualUsers.get(id).getName());
        List<User> userList = new ArrayList<>();
        for (int idUser : actualUsers.get(id).getFriendsId()) {
            userList.add(actualUsers.get(idUser));
        }
        return userList;
    }
}

