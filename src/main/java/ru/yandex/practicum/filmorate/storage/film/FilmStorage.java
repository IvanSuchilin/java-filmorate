package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film create(Film film);

    Film update(Film film);

    void delete(Film film);

    Optional<Film> getFilmById(long id);

    public void addLike(long id);

    public void removeLike(long id);
}
