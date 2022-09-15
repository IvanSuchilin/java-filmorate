package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.InvalidDescriptionLengthException;
import ru.yandex.practicum.filmorate.exception.InvalidDurationException;
import ru.yandex.practicum.filmorate.exception.InvalidFilmNameException;
import ru.yandex.practicum.filmorate.exception.InvalidRelaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    void filmWithEmptyName() {
        Film film = Film.builder()
                .name("")
                .id(1)
                .description("good film")
                .releaseDate(LocalDate.of(1990, 10, 10))
                .duration(360)
                .build();
        InvalidFilmNameException thrown = Assertions.assertThrows(InvalidFilmNameException.class,
                () -> filmController.validateFilm(film));
        Assertions.assertEquals("Название фильма не может быть пустым", thrown.getMessage());
    }

    @Test
    void filmWithWrongDate() {
        Film film = Film.builder()
                .name("film")
                .id(1)
                .description("good film")
                .releaseDate(LocalDate.of(1700, 10, 10))
                .duration(360)
                .build();
        InvalidRelaseDateException thrown = Assertions.assertThrows(InvalidRelaseDateException.class,
                () -> filmController.validateFilm(film));
        Assertions.assertEquals("Дата релиза не должна быть раньше 28.12.1985", thrown.getMessage());
    }

    @Test
    void filmWithWrongDuration() {
        Film film = Film.builder()
                .name("film")
                .id(1)
                .description("good film")
                .releaseDate(LocalDate.of(1980, 10, 10))
                .duration(-100)
                .build();
        InvalidDurationException thrown = Assertions.assertThrows(InvalidDurationException.class,
                () -> filmController.validateFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной", thrown.getMessage());
    }

    @Test
    void filmWithWrongDescription() {
        Film film = Film.builder()
                .name("film")
                .id(1)
                .description("Хронологическая предыстория трилогии «Властелин колец» — кинотрилогия Хоббит. " +
                                "Властелин колец является одним из самых крупных проектов в истории кино. " +
                                "Его реализация заняла восемь лет; все три фильма были сняты одновременно в " +
                                "Новой Зеландии, родной стране Питера Джексона. У каждого из фильмов трилогии " +
                        "есть специальная расширенная версия," +
                                "выпущенная на DVD спустя год после выхода соответствующей театральной версии. " +
                                "Фильмы следуют за основной сюжетной линией книги, но опускают некоторые " +
                        "существенные элементы " +
                                "повествования, включают дополнения и отклонения от исходного материала. " +
                                "Сюжет трилогии следует за хоббитом Фродо Бэггинсом, который идёт в " +
                                "поход вместе с Братством Кольца с целью уничтожения Кольца Всевластия. " +
                                "Это необходимо для окончательной победы над его создателем, Тёмным Властелином Сауроном, " +
                                "целью которого является захват всего Средиземья. Братство распадается, и Фродо " +
                                "продолжает путешествие вместе с верным спутником Сэмом и предательским " +
                        "проводником Голлумом " +
                                "Тем временем Арагорн, наследник трона " +
                                "Гондора, его товарищи эльф Леголас, гном Гимли и волшебник Гэндальф объединяют " +
                        "Свободные народы Средиземья для противостояния армиям Саурона в Войне Кольца.")
                .releaseDate(LocalDate.of(1980, 10, 10))
                .duration(100)
                .build();
        InvalidDescriptionLengthException thrown = Assertions.assertThrows(InvalidDescriptionLengthException.class,
                () -> filmController.validateFilm(film));
        Assertions.assertEquals("Максимальная длина описания должна быть 200 символов", thrown.getMessage());
    }
}
