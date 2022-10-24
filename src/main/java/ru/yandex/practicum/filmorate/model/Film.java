package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
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
    private String mpa;

    @JsonIgnore
    private Set<Like> likes = new HashSet<>();
    @JsonIgnore
    private Set<Genre> genres = new HashSet<>();
    @JsonIgnore
    private long rate = 0;

    /*public void addLike(long id){
        userId.add(id);
        rate = userId.size();
    }

    public void removeLike(long id){
        userId.remove(id);
        rate = userId.size();
    }*/
}
