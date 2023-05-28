package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

public class CommentMapper {
    public static Comment fromDto(CommentDto com, Item item, User author) {
        return Comment.builder()
                .id(com.getId())
                .item(item)
                .user(author)
                .created(com.getCreated())
                .text(com.getText())
                .build();
    }

    public static CommentDto toDto(Comment com) {
        return CommentDto.builder()
                .id(com.getId())
                .text(com.getText())
                .authorName(com.getUser().getName())
                .created(com.getCreated())
                .build();
    }
}
