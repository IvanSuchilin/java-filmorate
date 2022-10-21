package ru.yandex.practicum.filmorate.model;

import lombok.*;


    @Getter
    @Setter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Genre {
        private Integer CId;
        private String genreName;
    }

