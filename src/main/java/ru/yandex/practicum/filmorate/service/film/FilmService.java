package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private Validation validation;
    private FilmStorage filmStorage;
    private int filmId = 0;

    @Autowired
    public FilmService (Validation validation, InMemoryFilmStorage filmStorage){
        this.validation = validation;
        this.filmStorage = filmStorage;
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
        Film film = filmStorage.getFilms().get(id);
        validateFilm(film);
        filmStorage.delete(film);}
}
