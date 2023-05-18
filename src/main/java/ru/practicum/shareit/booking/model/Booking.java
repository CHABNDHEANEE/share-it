package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Date;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private long id;
    private Item item;
    private Date start;
    private Date end;
    private User booker;
    private BookingStatus status;
}
