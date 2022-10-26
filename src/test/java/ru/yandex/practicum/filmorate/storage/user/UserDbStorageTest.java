package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    User newUser;

    @BeforeAll
    void setUp(){
        newUser = userStorage.create(new User(1L, "@mail", "nick", "name",
                LocalDate.of(1990, 11, 14)));
    }

@Test
public void getAllUsersTest() {
    List<User> allUsers = userStorage.getAllUsers();
    assertEquals(0, allUsers.size(), "Количество всех пользователей не совпадает");
}
}