package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.ObjectExistenceException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class BookingMapper {
    public static BookingDto bookingToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .item(booking.getItem())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .status(toStatus(booking.getStatus()))
                .build();
    }

    public static Booking bookingFromDto(BookingDto booking, Item item, User booker) {
        return Booking.builder()
                .id(booking.getId())
                .item(item)
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booker)
                .status(toCondition(booking.getStatus(), booking.getStart(), booking.getEnd()))
                .build();
    }

    private static BookingCondition toCondition(BookingStatus status, LocalDateTime start, LocalDateTime end) {
        if (status == null)
            return BookingCondition.WAITING;
        switch (status) {
            case WAITING:
                return BookingCondition.WAITING;
            case REJECTED:
                return BookingCondition.REJECTED;
            case APPROVED:
                if (start.isAfter(LocalDateTime.from(Instant.now())) && end.isBefore(LocalDateTime.from(Instant.now())))
                    return BookingCondition.CURRENT;
                else if (end.isBefore(LocalDateTime.from(Instant.now())))
                    return BookingCondition.PAST;
                else
                    return BookingCondition.FUTURE;
            default:
                throw new ObjectExistenceException("");
        }
    }

    private static BookingStatus toStatus(BookingCondition status) {
        if (status == null)
            return BookingStatus.WAITING;
        switch (status) {
            case FUTURE:
            case CURRENT:
            case PAST:
                return BookingStatus.APPROVED;
            case REJECTED:
                return BookingStatus.REJECTED;
            case WAITING:
                return BookingStatus.WAITING;
            default:
                throw new ObjectExistenceException("");
        }
    }
}
