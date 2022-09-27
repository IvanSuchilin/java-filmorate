package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class User {

    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @JsonIgnore
    private List<Integer> friendsId;
}
