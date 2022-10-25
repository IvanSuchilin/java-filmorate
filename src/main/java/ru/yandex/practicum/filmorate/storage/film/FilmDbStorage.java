package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

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
        Set<Genre> genres = film.getGenres();
        film.setGenres(genres);
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
        String sqlQueryDelete = "delete from COMBI_FILMS_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDelete, film.getId());
        for (Genre genre : genres) {
            String sqlQueryGenre = "insert into COMBI_FILMS_GENRES(GENRE_ID, FILM_ID) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQueryGenre,
                    genre.getId(),
                    film.getId());
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

            return Optional.of(addGenresToFilm(film));
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    public Set<Genre> getGenresByFilmId(long id) {
        String sql = "SELECT  DISTINCT COMBI_FILMS_GENRES.GENRE_ID, GENRE_NAME FROM COMBI_FILMS_GENRES " +
                "JOIN GENRES G2 on G2.GENRE_ID = COMBI_FILMS_GENRES.GENRE_ID\n" +
                "                  WHERE COMBI_FILMS_GENRES.FILM_ID = ?" +
                "ORDER BY COMBI_FILMS_GENRES.GENRE_ID ASC";
        List<Genre> genreList = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Genre(
                        rs.getInt("GENRE_ID"),
                        rs.getString("GENRE_NAME")), id
        );
        return new HashSet<>(genreList);
    }

    public Film addGenresToFilm(Film film) {
        Collection<Genre> genres = getGenresByFilmId(film.getId());
        film.setGenres(getGenresByFilmId(film.getId()));
        return film;
    }

    public void addLike(int filmId, int userId){
        String sqlQuery = "INSERT INTO LIKES (CLIENT_ID, FILM_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                filmId
        );
    }

    public void removeLike(int filmId, int userId){
    String sqlQuery = "delete from LIKES where CLIENT_ID = ? AND FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
}

    @Override
    public List<Film> getPopularFilms() {
        String sql = "";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        new Film(
                                rs.getLong("FILM_ID"),
                                rs.getString("FILM_NAME"),
                                rs.getDate("RELEASE_DATE").toLocalDate(),
                                rs.getString("DESCRIPTION"),
                                rs.getLong("DURATION"),
                                new Mpa(rs.getInt("MPA_ID"),
                                        rs.getString("MPA_INFO"))));
    }
}

   /*@Override

    @Override
    public void delete(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new RuntimeException("Нет такого id");
        }
        films.remove(film.getId());
        log.debug("Пользователь с именем: {} удален", film.getName());
    }
*/

