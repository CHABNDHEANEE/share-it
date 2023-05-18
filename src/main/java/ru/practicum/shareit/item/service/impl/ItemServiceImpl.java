package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectAccessException;
import ru.practicum.shareit.exception.ObjectUpdateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final Map<Long, Item> items;
    private final UserService userService;
    private long curId = 1;

    @Override
    public ItemDto addItem(ItemDto item, Long userId) {
        item.setOwner(UserMapper.toUser(userService.getUser(userId)));
        item.setId(curId++);
        items.put(item.getId(), ItemMapper.toItem(item));
        return item;
    }

    @Override
    public ItemDto getItem(Long id) {
        return ItemMapper.toItemDto(items.get(id));
    }

    @Override
    public List<ItemDto> getItemsList(Long userId) {
        return items.values().stream()
                .filter(o -> o.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(Long id, ItemDto item, Long userId) {
        checkItemOnUpdate(id, item, userId);

        Item itemToUpdate = items.get(id);

        itemToUpdate.setName(item.getName() == null ? itemToUpdate.getName() : item.getName());
        itemToUpdate.setDescription(item.getDescription() == null ? itemToUpdate.getDescription() : item.getDescription());
        itemToUpdate.setAvailable(item.getAvailable() == null ? itemToUpdate.isAvailable() : item.getAvailable());

        items.put(id, itemToUpdate);
        return ItemMapper.toItemDto(itemToUpdate);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return items.values().stream()
                .filter(o -> (o.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        o.getName().toLowerCase().contains(text.toLowerCase())) &&
                        o.isAvailable() &&
                        !text.isBlank())
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void checkItemOwner(Item item, Long userId) {
        if (item.getOwner().getId() != userId) {
            throw new ObjectAccessException("You can edit only your items");
        }
    }

    private void checkItemOnUpdate(Long id, ItemDto item, Long userId) {
        checkItemOwner(items.get(id), userId);

        if (item.getOwner() != null || item.getRequest() != null)
            throw new ObjectUpdateException("These fields can't be updated");
    }
}
