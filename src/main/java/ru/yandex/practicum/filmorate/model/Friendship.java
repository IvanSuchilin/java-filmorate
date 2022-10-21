package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friendship {
    private Integer friendshipId;
    private Integer firstFriendId;
    private Integer secondFriendId;
}
