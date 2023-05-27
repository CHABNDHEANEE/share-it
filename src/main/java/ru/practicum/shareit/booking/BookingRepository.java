package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(long id);

    List<Booking> findAllByItemOwnerId(long id);

    Booking findDistinctFirstByItemIdOrderByStart(long itemId);

    Booking findDistinctFirstByItemIdOrderByEndAsc(long itemId);
}
