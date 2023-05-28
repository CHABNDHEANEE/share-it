package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.BookingDto;

import java.util.HashSet;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
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
                item.getComments() == null ? new HashSet<>() : item.getComments());

    }
}
