package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDaoImplTest {
private final MpaDaoImpl mpaDao;
    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql"})
    void getMpaByIdTest() {
        Optional<Mpa> mpaOptional = mpaDao.getMpaById(1);
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G")
                );
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/testSchema.sql",
            "file:dbTest/scripts/testData.sql"})
    void getAllMpaTest() {
        assertEquals(5, mpaDao.getAllMpa().size());
    }
}
