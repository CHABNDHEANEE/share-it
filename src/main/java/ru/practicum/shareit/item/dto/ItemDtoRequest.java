package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ItemDtoRequest {

    long id;

    String name;

    String description;

    boolean available;

    long requestId;
}
