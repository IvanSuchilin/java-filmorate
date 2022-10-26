package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.GenreDaoImpl;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addUsers.sql"})
    public void getAllUsersTest() {
        List<User> allUsers = userStorage.getAllUsers();
        assertEquals(2, allUsers.size(), "Количество всех пользователей не совпадает");
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addUsers.sql"})
    public void getUserById() {
        Optional<User> checker = userStorage.getUserById(1);
        assertThat(checker)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addUsers.sql"})
    public void deleteUserById() {
        userStorage.delete(1L);
        List<User> allUsers = userStorage.getAllUsers();
        assertEquals(1, allUsers.size(), "Количество всех пользователей не совпадает");
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addUsers.sql"})
    public void createUser() {
        User newUserTest = new User(3L, "@mail", "nick", "name",
                LocalDate.of(1991, 11, 14));
        userStorage.create(newUserTest);
        List<User> allUsers = userStorage.getAllUsers();
        assertEquals(3, allUsers.size(), "Количество всех пользователей не совпадает");
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addUsers.sql"})
    public void addFriendTest() {
        userStorage.addFriend(1, 2);
        List<User> friends = userStorage.getFriendsList(1);
        assertEquals(1, friends.size(), "Количество друзей не совпадает");
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addUsers.sql"})
    public void getFriendsListTest() {
        userStorage.addFriend(1, 2);
        List<User> friends = userStorage.getFriendsList(1);
        assertEquals(1, friends.size(), "Количество друзей не совпадает");
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addUsers.sql"})
    public void updateUserTest() {
        User newUserTest = new User(1L, "@mail", "nick", "name",
                LocalDate.of(1991, 11, 14));
        userStorage.update(newUserTest);
        Optional<User> checker = userStorage.getUserById(1);
        assertThat(checker)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "nick")
                );
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addUsers.sql"})
    public void deleteFriendTest() {
        userStorage.addFriend(1, 2);
        userStorage.deleteFriend(1, 2);
        List<User> friends = userStorage.getFriendsList(1);
        assertEquals(0, friends.size(), "Количество друзей не совпадает");
    }
}
