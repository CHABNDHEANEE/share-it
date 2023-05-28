package ru.practicum.shareit.booking;

public interface ItemBookingService {
    ItemBooking getLastBooking(long itemId);

    ItemBooking getNextBooking(long itemId);

    boolean checkBookingCompleted(long itemId, long userId);
}
