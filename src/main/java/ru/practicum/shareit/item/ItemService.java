package ru.practicum.shareit.item;

import ru.practicum.shareit.item.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto item, Long userId);

    ItemDto getItem(Long id, long userId);

    List<ItemDto> getItemsList(Long userId);

    ItemDto updateItem(Long id, ItemDto item, Long userId);

    List<ItemDto> searchItem(String text);
}
