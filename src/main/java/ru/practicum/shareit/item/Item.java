package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.user.User;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "items", schema = "public")
@AllArgsConstructor
@RequiredArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private boolean available;

    @ManyToOne
    private User owner;

    private Long request;

    @Transient
    private BookingDto lastBooking;

    @Transient
    private BookingDto nextBooking;

    @ElementCollection
    @CollectionTable(name = "comments", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "text")
    private Set<String> comments = new HashSet<>();
}
