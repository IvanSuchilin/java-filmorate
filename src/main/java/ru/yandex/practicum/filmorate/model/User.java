package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
/*public class User {

    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @JsonIgnore
    private List<Long> friendsId = new ArrayList<>();
}*/
public class User {

    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && name.equals(user.name) && email.equals(user.email)
                 && Objects.equals(birthday, user.birthday);
    }
}
