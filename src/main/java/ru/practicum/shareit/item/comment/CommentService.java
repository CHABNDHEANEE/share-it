package ru.practicum.shareit.item.comment;


import java.util.List;

public interface CommentService {
    CommentDto add(long itemId, CommentDto comment, long userId);

    List<CommentDto> getAllCommentsByItemId(long itemId);
}
