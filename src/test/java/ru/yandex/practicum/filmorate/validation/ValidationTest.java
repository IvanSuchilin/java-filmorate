package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ValidationTest {
   private Validation validation;
    final String LONG_DESCRIPTION = "Хронологическая предыстория трилогии «Властелин колец» — кинотрилогия Хоббит. " +
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
            "Свободные народы Средиземья для противостояния армиям Саурона в Войне Кольца.";
    final LocalDate GOOD_DATE = LocalDate.of(1990, 11, 14);

    @BeforeEach
    void setUp() {
        validation = new Validation();
    }


    @Test
    void userWithWrongEmailTest() {
        User user = createUser("mail", "login", "name", GOOD_DATE);
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> validation.validateUser(user));
        Assertions.assertEquals("Адрес электронной почты не может быть пустым и " +
                "должен содержать символ @", thrown.getReason());
    }

    @Test
    void userWithEmptyEmailTest() {
        User user = createUser("", "login", "name", GOOD_DATE);
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> validation.validateUser(user));
        Assertions.assertEquals("Адрес электронной почты не может быть пустым " +
                "и должен содержать символ @", thrown.getReason());
    }

    @Test
    void userWithWrongLoginTest() {
        User user = createUser("@mail", "log in", "name", GOOD_DATE);
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> validation.validateUser(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", thrown.getReason());
    }

    @Test
    void userWithEmptyLoginTest() {
        User user = createUser("@mail", "", "name", GOOD_DATE);
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> validation.validateUser(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", thrown.getReason());
    }

    @Test
    void userWithWrongDateTest() {
        User user = createUser("@mail", "login", "name", LocalDate.of(3000, 11, 14));
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> validation.validateUser(user));
        Assertions.assertEquals("Дата рождения не может быть в будущем", thrown.getReason());
    }

    @Test
    void userWithEmptyNameTest() {
        User user = createUser("@mail", "login", "", GOOD_DATE);
        validation.validateUser(user);
        assertEquals("login", user.getName());
    }

    @Test
    void filmWithEmptyName() {
        Film film = createFilm("", LONG_DESCRIPTION, GOOD_DATE, 360);
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> validation.validateFilm(film));
        Assertions.assertEquals("Название фильма не может быть пустым", thrown.getReason());
    }

    @Test
    void filmWithWrongDate() {
        Film film = createFilm("film", "good film", LocalDate.of(1700, 10, 10),
                360);
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> validation.validateFilm(film));
        Assertions.assertEquals("Дата релиза не должна быть раньше 28.12.1985", thrown.getReason());
    }

    @Test
    void filmWithWrongDuration() {
        Film film = createFilm("film", "good film", GOOD_DATE, -100);
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> validation.validateFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной", thrown.getReason());
    }

    @Test
    void filmWithWrongDescription() {
        Film film = createFilm("film", LONG_DESCRIPTION, GOOD_DATE, 100);
        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> validation.validateFilm(film));
        Assertions.assertEquals("Максимальная длина описания должна быть 200 символов", thrown.getReason());
    }

    private Film createFilm(String name, String description, LocalDate date, long duration) {
        return Film.builder()
                .name(name)
                .id(1)
                .description(description)
                .releaseDate(date)
                .duration(duration)
                .build();
    }

    private User createUser(String mail, String login, String name, LocalDate birthday) {
        return User.builder()
                .id(1)
                .email(mail)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }
}