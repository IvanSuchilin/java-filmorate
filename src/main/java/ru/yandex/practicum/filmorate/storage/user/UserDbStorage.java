package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public Map<Long, User> getUsers() {
        return null;
    } //ДОРАБОТАТЬ

    public List<User> getAllUsers() {

            String sql = "SELECT * FROM CLIENTS";

        return jdbcTemplate.query(
                    sql,
                    (rs, rowNum) ->
                            new User(
                                    rs.getLong("CLIENT_ID"),
                                    rs.getString("CLIENT_EMAIL"),
                                    rs.getString("LOGIN"),
                                    rs.getString("CLIENT_NAME"),
                                    rs.getDate("BIRTHDAY").toLocalDate()
                            )
            );
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into CLIENTS(CLIENT_ID, CLIENT_EMAIL, LOGIN, CLIENT_NAME, BIRTHDAY) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        return user;
    }


    @Override
    public User update(User user) {
        String sqlQuery = "update CLIENTS set " +
                "CLIENT_EMAIL = ?, LOGIN = ?, CLIENT_NAME = ?, BIRTHDAY = ?" +
                "where CLIENT_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
        user.getId());
        return user;
    }

    @Override
    public void delete(Long id) {
        //Long id = user.getId();
        String sqlQuery = "delete from CLIENTS where CLIENT_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Optional<User> getUserById(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from CLIENTS where CLIENT_ID = ?", id);
        if(userRows.next()) {
            User user = new User(
                    userRows.getLong("CLIENT_ID"),
                    userRows.getString("CLIENT_EMAIL"),
                    userRows.getString("LOGIN"),
                    userRows.getString("CLIENT_NAME"),
                    userRows.getDate("BIRTHDAY").toLocalDate()
            );
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }
}
