package ru.practicum.shareit.review.model;

import lombok.Data;
import ru.practicum.shareit.user.User;

@Data
public class Review {
    private long id;
    private User author;
    private String review;
}
