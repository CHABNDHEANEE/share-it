package ru.practicum.shareit.booking;


import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.Date;

@Data
@Builder
public class BookingDto {
    private long id;
    private Item item;
    private Date start;
    private Date end;
    private User booker;
    private BookingStatus status;
}
