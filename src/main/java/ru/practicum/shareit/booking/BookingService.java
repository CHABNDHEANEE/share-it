package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.util.List;

interface BookingService {
    void add(Booking booking);

    Booking approve(long bookingId, boolean status, long userId);

    Booking get(long bookingId, long userId);

    List<Booking> getAllByUser(long userId, BookingStatus status);

    List<Booking> getBookingsByItems(long userId, BookingStatus status);
}
