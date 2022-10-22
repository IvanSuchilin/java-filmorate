package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public User create(User user) {
        users.put(user.getId(), user);
        log.debug("Создан новый пользователь с именем: {}", user.getName());
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new RuntimeException("Нет такого id");
        }
        users.put(user.getId(), user);
        log.debug("Обновлены данные пользователя с именем: {}", user.getName());
        return user;
    }

    @Override
    public void delete(Long id) {
        if (!users.containsKey(id)) {
            throw new RuntimeException("Нет такого id");
        }
        users.remove(id);
        log.debug("Пользователь с id: {} удален", id);
    }

    @Override
    public Optional<User> getUserById(long id) {
        Map<Long, User> actualUsers = getUsers();
        if (!actualUsers.containsKey(id)) {
            throw new DataNotFoundException("Нет такого id - пользователя");
        }
        return Optional.ofNullable(actualUsers.get(id));
    }
}
