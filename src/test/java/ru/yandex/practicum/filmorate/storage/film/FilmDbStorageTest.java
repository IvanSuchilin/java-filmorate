package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;


    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addFilms.sql"})
    void getAllFilmsTest() {
        List<Film> allFilms = filmDbStorage.getAllFilms();
        assertEquals(2, allFilms.size());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addFilms.sql"})
    void createTest() {
        Film newFilm = new Film(3L, "name3", LocalDate.of(2000, 10, 12),
                "java3", 60, new Mpa(1, "G"));
        filmDbStorage.create(newFilm);
        Optional<Film> filmOptional = filmDbStorage.getFilmById(3);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "java3")
                );
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addFilms.sql"})
    void createWrongFilmTest() {
        Film newFilm = new Film(3L, "name3", LocalDate.of(1500, 10, 12),
                "java3", 60, new Mpa(1, "G"));
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> filmDbStorage.update(newFilm));
        Assertions.assertEquals("Дата релиза не должна быть раньше 28.12.1985", thrown.getReason());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addFilms.sql"})
    void updateTest() {
        Film newFilm = new Film(2L, "name32", LocalDate.of(2000, 10, 12),
                "java3", 60, new Mpa(1, "G"));
        filmDbStorage.update(newFilm);
        Optional<Film> filmOptional = filmDbStorage.getFilmById(2);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "name32")
                );
    }
    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addFilms.sql"})
    void updateWrongFilmTest() {
        Film newFilm = new Film(2L, "name32", LocalDate.of(2000, 10, 12),
                "java3", -60, new Mpa(1, "G"));
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> filmDbStorage.update(newFilm));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной", thrown.getReason());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addFilms.sql"})
    void updateGenreTest() {
        Film newFilm = new Film(2L, "name32", LocalDate.of(2000, 10, 12),
                "java3", 60, new Mpa(1, "G"));
        newFilm.setGenres(List.of(new Genre(6, "Боевик")));
        filmDbStorage.update(newFilm);
        Optional<Film> filmOptional = filmDbStorage.getFilmById(2);
        assertEquals("Боевик", filmOptional.get().getGenres().get(0).getName());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addFilms.sql"})
    void deleteTest() {
        filmDbStorage.delete(1);
        List<Film> allFilms = filmDbStorage.getAllFilms();
        assertEquals(1, allFilms.size());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addFilms.sql"})
    void getFilmByIdTest() {
        Optional<Film> filmOptional = filmDbStorage.getFilmById(2);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "java2")
                );
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addFilms.sql", "file:dbTest/scripts/addUsers.sql"})
    void addLikeTest() {
        filmDbStorage.addLike(2, 1);
        Optional<Film> filmOptional = filmDbStorage.getFilmById(2);
        assertEquals(1, filmOptional.get().getRate());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addFilms.sql", "file:dbTest/scripts/addUsers.sql"})
    void removeLikeTest() {
        filmDbStorage.addLike(2, 1);
        filmDbStorage.removeLike(2, 1);
        Optional<Film> filmOptional = filmDbStorage.getFilmById(2);
        assertEquals(0, filmOptional.get().getRate());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql", "file:dbTest/scripts/addFilms.sql", "file:dbTest/scripts/addUsers.sql"})
    void getPopularFilmsTest() {
        filmDbStorage.addLike(2, 1);
        List<Film> popFilm = filmDbStorage.getPopularFilms(2);
        assertEquals(2, popFilm.get(0).getId());
    }
}
