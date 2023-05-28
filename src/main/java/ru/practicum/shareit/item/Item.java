package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.ItemBooking;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "items", schema = "public")
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
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

    @OneToOne
    private ItemRequest request;

    @Transient
    private ItemBooking lastBooking;

    @Transient
    private ItemBooking nextBooking;

    @Transient
    private List<CommentDto> comments;
}
