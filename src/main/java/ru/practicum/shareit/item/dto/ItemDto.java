package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemDto {
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    @NotNull
    private User owner;
    @NotNull
    private Long request;
}
