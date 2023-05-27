package ru.practicum.shareit.booking;

import java.util.Optional;

public interface ItemBookingService {
    Optional<Booking> getLastBooking(long itemId);

    Optional<Booking> getNextBooking(long itemId);

    boolean checkBookingCompleted(long itemId, long userId);
}
