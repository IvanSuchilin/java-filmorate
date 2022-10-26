package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDaoImplTest {
    private final GenreDaoImpl genreDao;

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql"})
    void getGenreByIdTest() {
        Optional<Genre> genreOptional = genreDao.getGenreById(6);
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Боевик")
                );
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql"})
    void getAllGenreTest() {
        assertEquals(6, genreDao.getAllGenre().size());
    }
}
