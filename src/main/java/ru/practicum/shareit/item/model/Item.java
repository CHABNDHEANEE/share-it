package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class Item {
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private boolean available;
    @NotNull
    private User owner;
    @NotNull
    private Long request;
}
