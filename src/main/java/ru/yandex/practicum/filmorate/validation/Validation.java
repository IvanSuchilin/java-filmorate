package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
@Service
public class Validation {

    public void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Название фильма не может быть пустым");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза не должна быть раньше 28.12.1985");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Дата релиза не должна быть раньше 28.12.1985");
        }
        if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания должна быть 200 символов");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Максимальная длина описания должна быть 200 символов");
        }
        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Продолжительность фильма должна быть положительной");
        }
    }

    public void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Адрес электронной почты не может быть пустым и должен содержать символ @");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Адрес электронной почты не может быть пустым и должен содержать символ @");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Дата рождения не может быть в будущем");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.error("Имя пользователя пусто - используется логин в качестве имени");
            user.setName(user.getLogin());
        }
    }
}
