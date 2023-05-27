package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Builder
@Table(name = "bookings", schema = "public")
@RequiredArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @Column(name = "start_date")
    private Date start;

    @Column(name = "end_date")
    private Date end;

    @ManyToOne
    private User booker;

    @Enumerated(EnumType.STRING)
    private BookingCondition status = BookingCondition.WAITING;
}
