package ru.yandex.practicum.filmorate.model;

import lombok.*;


    @Getter
    @Setter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Genre {
        private Integer id;
        private String name;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Genre genre = (Genre) o;
            return id.equals(genre.id) && name.equals(genre.name);
        }
    }

