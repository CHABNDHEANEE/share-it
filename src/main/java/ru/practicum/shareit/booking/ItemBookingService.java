package ru.practicum.shareit.booking;

public interface ItemBookingService {
    Booking getLastBooking(long itemId);

    Booking getNextBooking(long itemId);
}
