package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users = new HashMap<>();

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    /*@Override
    public void setUsers(Map<Integer, User> users) {
        this.users = users;
    }*/

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
    public void delete(User user) {
        if (!users.containsKey(user.getId())) {
            throw new RuntimeException("Нет такого id");
        }
        users.remove(user.getId());
        log.debug("Пользователь с именем: {} удален", user.getName());
    }
}
