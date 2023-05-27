package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.item.comment.Comment;

import java.util.List;

public interface CommentService {
    Comment add(long itemId, Comment comment, long userId);

    List<Comment> getCommentsForItem(long itemId);

    List<Comment> getCommentsForUser(long userId);
}
