package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;


public class BookingMapper {
    public static BookingDto bookingToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .item(booking.getItem())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public static Booking bookingFromDto(BookingDto booking, Item item, User booker) {
        return Booking.builder()
                .id(booking.getId())
                .item(item)
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booker)
                .status(booking.getStatus() == null ? BookingStatus.WAITING : booking.getStatus())
                .build();
    }
}
