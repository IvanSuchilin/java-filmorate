package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.Optional;

public interface UserStorage {

     Map<Long, User> getUsers();

    User create(User user);

    User update(User user);

     void delete(Long id);

    Optional<User> getUserById(long id);
}
