package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.exception.ObjectAccessException;

import java.util.HashSet;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        try {
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.isAvailable())
                    .owner(item.getOwner())
                    .request(item.getRequest())
                    .lastBooking(item.getLastBooking())
                    .nextBooking(item.getNextBooking())
                    .comments(item.getComments())
                    .build();
        } catch (Exception e) {
            throw new ObjectAccessException("error from toDto " + e.getMessage());
        }
    }

    public static Item toItem(ItemDto item) {
        return new Item(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest(),
                new BookingDto(),
                new BookingDto(),
                new HashSet<>());

    }
}
