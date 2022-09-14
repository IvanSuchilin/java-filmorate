package ru.yandex.practicum.filmorate.exception;

public class InvalidDurationException extends RuntimeException {
    public InvalidDurationException(String s) {
        super(s);
    }
}
