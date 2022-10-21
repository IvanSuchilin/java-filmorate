package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mpa {
    private Integer MpaId;
    private String MpaInfo;
}