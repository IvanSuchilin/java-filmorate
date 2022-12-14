package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {

    private Long id;
    private String name;
    private LocalDate releaseDate;
    private String description;
    private long duration;
    private Mpa mpa;
    @JsonIgnore
    private Set<Long> likes = new HashSet<>();
    private List<Genre> genres = new ArrayList<>();
    @JsonIgnore
    private long rate = 0;

    public Film(Long id, String name, LocalDate releaseDate, String description, long duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.description = description;
        this.duration = duration;
        this.mpa = mpa;
    }
}
