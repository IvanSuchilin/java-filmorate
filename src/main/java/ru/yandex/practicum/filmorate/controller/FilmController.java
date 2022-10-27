package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Создание фильма");
        return filmService.create(film);
    }

    @PutMapping
    public Optional<Film> put(@RequestBody Film film) {
        log.info("Обновление фильма с Id {}", film.getId());
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Optional<Film> getFilmById(@PathVariable("id") Integer id) {
        log.info("Получение данных фильма с идентификатором {}", id);
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("Добавление лайка фильму {} от пользователя {}", userId, id);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("Удаление лайка фильму {} от пользователя {}", userId, id);
        filmService.deleteLike(id, userId);
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Получение списка всех фильмов");
        return filmService.findAll();
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получение самого популярного фильма");
        return filmService.getPopularFilms(count);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        log.info("Удаление фильма с id {}", id);
        filmService.delete(id);
    }
}



