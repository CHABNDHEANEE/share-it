package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
    private long id;
    private String text;
}
