package ru.practicum.shareit.booking;

public interface ItemBookingService {
    BookingDto getLastBooking(long itemId);

    BookingDto getNextBooking(long itemId);

    boolean checkBookingCompleted(long itemId, long userId);
}
