package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {
    private Integer likeId;
    private Integer clientId;
    private Integer filmId;
}
