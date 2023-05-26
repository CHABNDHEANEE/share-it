package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Entity
@RequiredArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column
    private String name;

    @NotNull
    @Column
    private String description;

    @NotNull
    @Column
    private boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column
    private User owner;

    @Column
    private Long request;
}
