package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.ObjectExistenceException;

import java.time.Instant;
import java.util.Date;

public class BookingMapper {
    public static BookingDto bookingToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .item(booking.getItem())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .status(toStatus(booking.getStatus()))
                .build();
    }

    public static Booking bookingFromDto(BookingDto booking) {
        return Booking.builder()
                .id(booking.getId())
                .item(booking.getItem())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .status(toCondition(booking.getStatus(), booking.getStart(), booking.getEnd()))
                .build();
    }

    private static BookingCondition toCondition(BookingStatus status, Date start, Date end) {
        switch (status) {
            case WAITING:
                return BookingCondition.WAITING;
            case REJECTED:
                return BookingCondition.REJECTED;
            case APPROVED:
                if (start.after(Date.from(Instant.now())) && end.before(Date.from(Instant.now())))
                    return BookingCondition.CURRENT;
                else if (end.before(Date.from(Instant.now())))
                    return BookingCondition.PAST;
                else
                    return BookingCondition.FUTURE;
            default:
                throw new ObjectExistenceException("");
        }
    }

    private static BookingStatus toStatus(BookingCondition status) {
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
