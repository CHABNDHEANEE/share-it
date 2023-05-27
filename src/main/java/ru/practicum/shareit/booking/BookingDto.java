package ru.practicum.shareit.booking;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class BookingDto {
    private long id;
    @NotNull
    private long itemId;
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @NotNull
    @Future
    private LocalDateTime end;
    private User booker;
    private BookingStatus status;
}
