package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.Optional;

@Slf4j
@Service
public class FilmService {
    private final Validation validation;
    private final FilmStorage filmDbStorage;
    private final UserStorage userDbStorage;
    private Long filmId = 0L;

    @Autowired
    public FilmService(Validation validation, FilmStorage filmDbStorage, UserStorage userDbStorage) {
        this.validation = validation;
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public void validateFilm(Film film) {
        validation.validateFilm(film);
    }
    public Film create(Film film) {
        log.debug("Получен запрос POST /films.");
        validateFilm(film);
        filmId++;
        film.setId(filmId);
        return filmDbStorage.create(film);
    }
        public Film update(Film filmUp) {
            log.debug("Получен запрос PUT /films.");
           // Film film = filmDbStorage.getFilmById(filmUp.getId());
           // validateFilm(filmUp);
            return filmDbStorage.update(filmUp);
        }

    public Optional<Film> getFilm(long id) {
        log.debug("Получен запрос GET /films/{id}.");
        if (filmDbStorage.getFilmById(id).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        return filmDbStorage.getFilmById(id);
    }
    }



   /* public List<Film> findAll() {
        log.debug("Получен запрос GET /films.");
        log.debug("Текущее количество фильмов: {}", filmStorage.getFilms().size());
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    public Film create(Film film) {
        log.debug("Получен запрос POST /films.");
        validateFilm(film);
        filmId++;
        film.setId(filmId);
        return filmStorage.create(film);
    }

    public Film update(Film filmUp) {
        log.debug("Получен запрос PUT /films.");
        Film film = filmStorage.getFilmById(filmUp.getId());
        validateFilm(filmUp);
        return filmStorage.update(filmUp);
    }

    public void delete(long id) {
        log.debug("Получен запрос DELETE /films/{id}.");
        Film film = filmStorage.getFilmById(id);
        validateFilm(film);
        filmStorage.delete(film);
    }

    public void addLike(long id, long userId) {
        log.debug("Получен запрос PUT /films/{id}/like/{userId}.");
        Film film = filmStorage.getFilmById(id);
        User user = userStorage.getUserById(userId);
        film.addLike(userId);
    }

    public void deleteLike(long id, long userId) {
        log.debug("Получен запрос DELETE /films/{id}/like/{userId}.");
        Film film = filmStorage.getFilmById(id);
        User user = userStorage.getUserById(userId);
        film.removeLike(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilm(long id) {
        log.debug("Получен запрос GET /films/{id}.");
        return filmStorage.getFilmById(id);
    }*/

