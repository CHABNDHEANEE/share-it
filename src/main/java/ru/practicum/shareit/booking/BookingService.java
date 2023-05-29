package ru.practicum.shareit.booking;

import java.util.List;

interface BookingService {
    BookingDto addBooking(BookingDto booking, long userId);

    BookingDto approveBooking(long bookingId, boolean status, long userId);

    BookingDto getBooking(long bookingId, long userId);

    List<BookingDto> getAllByUser(long userId, BookingCondition status);

    List<BookingDto> getBookingsByItems(long userId, BookingCondition status);
}
