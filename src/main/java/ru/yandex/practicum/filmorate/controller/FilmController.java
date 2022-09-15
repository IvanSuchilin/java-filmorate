package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

        private Map<Integer, Film> films =  new HashMap<>();
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
            validateFilm(film);
            films.put(film.getId(), film);
            log.debug("Добавлен новый фильм с названием: {}", film.getName());
            return film;
        }

        @PutMapping
        public Film put(@RequestBody Film film) {
            log.debug("Получен запрос PUT /films.");
            validateFilm(film);
            if (!films.containsKey(film.getId())){
                throw new RuntimeException("Нет такого id");
            }
            films.put(film.getId(), film);
            log.debug("Обновлены данные фильма с названием: {}", film.getName());
            return film;
        }

        void validateFilm(Film film){
            if(film.getName() == null || film.getName().isBlank()) {
                log.error("Название фильма не может быть пустым");
                throw new InvalidFilmNameException("Название фильма не может быть пустым");
            }
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.error("Дата релиза не должна быть раньше 28.12.1985");
                throw new InvalidRelaseDateException("Дата релиза не должна быть раньше 28.12.1985");
            }
            if (film.getDescription().length() > 200){
                log.error("Максимальная длина описания должна быть 200 символов");
                throw new InvalidDescriptionLengthException("Максимальная длина описания должна быть 200 символов");
            }
            if (film.getDuration() <= 0){
                log.error("Продолжительность фильма должна быть положительной");
                throw new InvalidDurationException("Продолжительность фильма должна быть положительной");
            }
        }

}
