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

    private  int id;
    private String name;
    private LocalDate releaseDate;
    private String description;
    private long duration;

    @JsonIgnore
    private Set<Integer> userId = new HashSet<>();
    @JsonIgnore
    private long rate = 0;

    public void addLike(Integer id){
        userId.add(id);
        rate = userId.size();
    }

    public void removeLike(Integer id){
        userId.remove(id);
        rate = userId.size();
    }
}
