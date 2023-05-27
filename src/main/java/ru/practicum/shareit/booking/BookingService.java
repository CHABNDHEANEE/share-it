package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.util.List;

interface BookingService {
    BookingDto add(BookingDto booking, long userId);

    BookingDto approve(long bookingId, boolean status, long userId);

    BookingDto get(long bookingId, long userId);

    List<BookingDto> getAllByUser(long userId, BookingCondition status);

    List<BookingDto> getBookingsByItems(long userId, BookingCondition status);
}
