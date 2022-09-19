package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Film {

    private  int id;
    private String name;
    private LocalDate releaseDate;
    private String description;
    private long duration;
}
