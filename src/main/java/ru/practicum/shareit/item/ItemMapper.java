package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.BookingDto;

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
                new BookingDto());
    }
}
