package ru.practicum.shareit.booking;

import java.util.Optional;

public interface ItemBookingService {
    BookingDto getLastBooking(long itemId);

    BookingDto getNextBooking(long itemId);

    boolean checkBookingCompleted(long itemId, long userId);
}
