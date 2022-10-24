package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
    private Set<Like> likes = new HashSet<>();

   // private Set<Genre> genres = new HashSet<>();
    private Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));

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


    public void addGenre(Genre genre){
        genres.add(genre);
    }
    /*public void addLike(long id){
        userId.add(id);
        rate = userId.size();
    }

    public void removeLike(long id){
        userId.remove(id);
        rate = userId.size();
    }*/
}
