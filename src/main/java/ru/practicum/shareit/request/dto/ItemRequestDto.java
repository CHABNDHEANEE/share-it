package ru.practicum.shareit.request.dto;


import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoRequest;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class ItemRequestDto {

   private long id;

    @NotBlank
    private String description;

   private Date created;

   private List<ItemDtoRequest> items;
}
