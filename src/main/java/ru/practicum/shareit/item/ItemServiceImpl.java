package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.ItemBookingService;
import ru.practicum.shareit.exception.ObjectAccessException;
import ru.practicum.shareit.exception.ObjectExistenceException;
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
    private final ItemBookingService bookingService;
    private final ItemRepository repository;

    @Override
    public ItemDto addItem(ItemDto item, Long userId) {
        item.setOwner(UserMapper.toUser(userService.getUser(userId)));
        return ItemMapper.toItemDto(repository.save(ItemMapper.toItem(item)));
    }

    @Override
    public ItemDto getItem(Long id) {
        ItemDto item = ItemMapper.toItemDto(getItemById(id));
//        item.setLastBooking(bookingService.getLastBooking(item.getId()).orElse(null));
//        item.setNextBooking(bookingService.getNextBooking(item.getId()).orElse(null));
        item.setLastBooking(bookingService.getLastBooking(item.getId()));
        item.setNextBooking(bookingService.getNextBooking(item.getId()));
        return item;
    }

    @Override
    public List<ItemDto> getItemsList(Long userId) {
        return repository.findAllByOwnerId(userId).stream()
                .map(o -> {
//                    o.setLastBooking(bookingService.getLastBooking(o.getId()).orElse(null));
//                    o.setNextBooking(bookingService.getNextBooking(o.getId()).orElse(null));
                    o.setLastBooking(bookingService.getLastBooking(o.getId()));
                    o.setNextBooking(bookingService.getNextBooking(o.getId()));

                    return ItemMapper.toItemDto(o);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
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

        return repository.findAllByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(text, text)
                .stream()
                .filter(Item::isAvailable)
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

    private Item getItemById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectExistenceException("Item doesn't exists"));
    }
}
