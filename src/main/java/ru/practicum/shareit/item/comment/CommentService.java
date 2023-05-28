package ru.practicum.shareit.item.comment;


public interface CommentService {
    CommentDto add(long itemId, CommentDto comment, long userId);
}
