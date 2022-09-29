package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private Validation validation;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private int filmId = 0;

    @Autowired
    public FilmService (Validation validation, InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage){
        this.validation = validation;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;

    }
    public void validateFilm(Film film){
        validation.validateFilm(film);
    }

    public List<Film> findAll() {
        log.debug("Получен запрос GET /films.");
        log.debug("Текущее количество фильмов: {}", filmStorage.getFilms().size());
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    public Film create(Film film) {
        log.debug("Получен запрос POST /films.");
        filmId++;
        film.setId(filmId);
        validateFilm(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        log.debug("Получен запрос PUT /films.");
        validateFilm(film);
        return filmStorage.update(film);
    }

    public void delete(Integer id){
        log.debug("Получен запрос DELETE /films/{id}.");
        Map<Integer, Film> actualFilms = filmStorage.getFilms();
        if (!actualFilms.containsKey(id)) {
            throw new DataNotFoundException("Нет такого id");
        }
        Film film = actualFilms.get(id);
        validateFilm(film);
        filmStorage.delete(film);}

    public void addLike(int id, int userId) {
        log.debug("Получен запрос PUT /films/{id}/like/{userId}.");
        Map<Integer, Film> actualFilms = filmStorage.getFilms();
        Map<Integer, User> actualUsers = userStorage.getUsers();
        if (!actualFilms.containsKey(id) || !actualUsers.containsKey(userId)) {
            throw new DataNotFoundException("Нет такого id");
        }
        Film film = actualFilms.get(id);
        film.addLike(userId);
    }

    public void deleteLike(int id, int userId) {
        log.debug("Получен запрос DELETE /films/{id}/like/{userId}.");
        Map<Integer, Film> actualFilms = filmStorage.getFilms();
        Map<Integer, User> actualUsers = userStorage.getUsers();
        if (!actualFilms.containsKey(id) || !actualUsers.containsKey(userId)) {
            throw new DataNotFoundException("Нет такого id");
        }
        Film film = actualFilms.get(id);
        film.removeLike(userId);
    }

    public List<Film> getPopularFilms(int count) {

        return new ArrayList<>(filmStorage.getFilms().values()).stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(Integer id) {
        log.debug("Получен запрос GET /films/{id}.");
        Map<Integer, Film> actualFilms = filmStorage.getFilms();
        if (!actualFilms.containsKey(id)) {
            throw new DataNotFoundException("Нет такого id");
        }
        return actualFilms.get(id);
    }
}
