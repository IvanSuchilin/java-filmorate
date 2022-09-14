package ru.yandex.practicum.filmorate.exception;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String s) {
        super(s);
    }
}
