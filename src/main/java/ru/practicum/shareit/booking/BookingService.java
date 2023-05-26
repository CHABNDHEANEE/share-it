package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.util.List;

interface BookingService {
    void add(Booking booking, long userId);

    Booking approve(long bookingId, boolean status, long userId);

    Booking get(long bookingId, long userId);

    List<Booking> getAllByUser(long userId, BookingCondition status);

    List<Booking> getBookingsByItems(long userId, BookingCondition status);
}
