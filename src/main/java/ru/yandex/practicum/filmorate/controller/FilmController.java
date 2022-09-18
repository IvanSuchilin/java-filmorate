package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    Validation validation = new Validation();
    private int filmId = 0;

    @GetMapping
    public List<Film> findAll() {
        log.debug("Получен запрос GET /films.");
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.debug("Получен запрос POST /films.");
        filmId++;
        film.setId(filmId);
        validation.validateFilm(film);
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм с названием: {}", film.getName());
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        log.debug("Получен запрос PUT /films.");
        validation.validateFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new RuntimeException("Нет такого id");
        }
        films.put(film.getId(), film);
        log.debug("Обновлены данные фильма с названием: {}", film.getName());
        return film;
    }
}
