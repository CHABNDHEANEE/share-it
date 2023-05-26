package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Table(name = "bookings", schema = "public")
@RequiredArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Item item;

    @Column(name = "start_date")
    private Date start;

    @Column(name = "end_date")
    private Date end;

    @OneToOne
    private User booker;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
