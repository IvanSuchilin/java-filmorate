package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

     Map<Integer, User> getUsers();

    void setUsers(Map<Integer, User> users);

    User create(User user);

    User put(User user);

     void delete(User user);
}
