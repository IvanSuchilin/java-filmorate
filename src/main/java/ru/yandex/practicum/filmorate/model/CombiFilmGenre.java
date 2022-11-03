package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CombiFilmGenre {
    private Integer combiId;
    private Integer genreId;
    private Integer filmId;
}
