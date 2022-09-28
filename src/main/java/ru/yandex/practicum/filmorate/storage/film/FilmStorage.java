package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface FilmStorage {
    Map<Integer,Film> getFilms();

    Film create(Film film);

    Film update(Film film);

    void delete(Film film);
}
