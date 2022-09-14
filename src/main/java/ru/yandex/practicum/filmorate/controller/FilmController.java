package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

        private Map<Integer, Film> films =  new HashMap<>();
        private int filmId = 0;

        @GetMapping
        public List<Film> findAll() {
            return new ArrayList<>(films.values());
        }

        @PostMapping
        public Film create(@RequestBody Film film) {
            film.setId(filmId++);
            validateUser(film);
            films.put(film.getId(), film);
            return film;
        }

        @PutMapping
        public Film put(@RequestBody Film film) {
            validateUser(film);
            films.put(film.getId(), film);
            return film;
        }

        private void validateUser (Film film){
            if(film.getName() == null || film.getName().isBlank()) {
                throw new InvalidFilmNameException("Название фильма н может быть пустым");
            }
            if (film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))) {
                throw new InvalidRelaseDateException("Дата релиза не должна быть раньше 28.12.1985");
            }
            if (film.getDescription().length() > 200){
                throw new InvalidDescriptionLengthException("Максимальная длина описания должна быть 200 символов");
            }
            if (film.getDuration().getSeconds() <= 0){
                throw new InvalidDurationException("Продолжительность фильма должна быть положительной");
            }
        }

}
