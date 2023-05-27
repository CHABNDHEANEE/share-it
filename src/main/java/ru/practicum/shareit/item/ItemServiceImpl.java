package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectAccessException;
import ru.practicum.shareit.exception.ObjectUpdateException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository repository;
    private long curId = 1;

    @Override
    public ItemDto addItem(ItemDto item, Long userId) {
        item.setOwner(UserMapper.toUser(userService.getUser(userId)));
        item.setId(curId++);
        return ItemMapper.toItemDto(repository.save(ItemMapper.toItem(item)));
    }

    @Override
    public ItemDto getItem(Long id) {
        return ItemMapper.toItemDto(repository.getReferenceById(id));
    }

    @Override
    public List<ItemDto> getItemsList(Long userId) {
        return repository.findAllByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(Long id, ItemDto item, Long userId) {
        Item itemToUpdate = repository.findById(id).get();

        checkItemOnUpdate(itemToUpdate, item, userId);

        itemToUpdate.setName(item.getName() == null ? itemToUpdate.getName() : item.getName());
        itemToUpdate.setDescription(item.getDescription() == null ? itemToUpdate.getDescription() : item.getDescription());
        itemToUpdate.setAvailable(item.getAvailable() == null ? itemToUpdate.isAvailable() : item.getAvailable());

        repository.save(itemToUpdate);
        return ItemMapper.toItemDto(itemToUpdate);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isBlank())
            return new ArrayList<>();

        return repository.findAllByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseAndAvailableTrue(text, text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void checkItemOwner(Item item, Long userId) {
        if (item.getOwner().getId() != userId) {
            throw new ObjectAccessException("You can edit only your items");
        }
    }

    private void checkItemOnUpdate(Item itemToUpdate, ItemDto item, Long userId) {
        checkItemOwner(itemToUpdate, userId);

        if (item.getOwner() != null || item.getRequest() != null)
            throw new ObjectUpdateException("These fields can't be updated");
    }
}
