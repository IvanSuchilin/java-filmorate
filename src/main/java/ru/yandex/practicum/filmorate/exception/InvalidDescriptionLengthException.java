package ru.yandex.practicum.filmorate.exception;

public class InvalidDescriptionLengthException extends RuntimeException {
    public InvalidDescriptionLengthException(String s) {
        super(s);
    }
}
