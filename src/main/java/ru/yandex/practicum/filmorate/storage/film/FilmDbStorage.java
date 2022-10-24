package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return null;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into FILMS(FILM_ID, FILM_NAME,  RELEASE_DATE, DESCRIPTION, DURATION, MPA_ID) " +
                "values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId());
        if (film.getGenres().size() != 0) {
            for (Genre genre : film.getGenres()) {
                String sqlQueryGenre = "insert into COMBI_FILMS_GENRES(GENRE_ID, FILM_ID) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQueryGenre,
                        genre.getId(),
                        film.getId());
            }
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILMS SET FILM_ID = ?, FILM_NAME = ?,  RELEASE_DATE =?, " +
                "DESCRIPTION = ?, DURATION =?, MPA_ID=? WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (film.getGenres().size() != 0) {
            String sqlQueryDelete = "delete from COMBI_FILMS_GENRES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQueryDelete, film.getId());
            for (Genre genre : film.getGenres()) {
                String sqlQueryGenre = "insert into COMBI_FILMS_GENRES(GENRE_ID, FILM_ID) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQueryGenre,
                        genre.getId(),
                        film.getId());
            }
        }
        return film;
    }

    @Override
    public void delete(Film film) {

    }

    @Override
    public Optional<Film> getFilmById(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILMS F " +
                "JOIN MPA M ON F.MPA_ID = M.MPA_ID where FILM_ID = ?", id);
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong("FILM_ID"),
                    filmRows.getString("FILM_NAME"),
                    filmRows.getDate("RELEASE_DATE").toLocalDate(),
                    filmRows.getString("DESCRIPTION"),
                    filmRows.getLong("DURATION"),
                    new Mpa(filmRows.getInt("MPA_ID"),
                            filmRows.getString("MPA_INFO")));
            //наполнить фильм жанрами?
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public void addLike(long id) {

    }

    @Override
    public void removeLike(long id) {

    }

   /* @Override
    public Film create(Film film) {
        films.put(film.getId(), film);
        log.debug("Создан новый фильм с названием: {}", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new RuntimeException("Нет такого id");
        }
        films.put(film.getId(), film);
        log.debug("Обновлены данные пользователя с именем: {}", film.getName());
        return film;
    }

    @Override
    public void delete(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new RuntimeException("Нет такого id");
        }
        films.remove(film.getId());
        log.debug("Пользователь с именем: {} удален", film.getName());
    }

    @Override
    public Film getFilmById(long id){
        Map<Long, Film> actualFilms = getFilms();
        if (!actualFilms.containsKey(id)) {
            throw new DataNotFoundException("Нет такого id - фильма");
        }
        return actualFilms.get(id);
    }*/
}
