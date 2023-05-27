package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(long id);

    List<Booking> findAllByItemOwnerId(long id);

    Booking findDistinctFirstByItemIdOrderByStart(long itemId);

    Booking findDistinctFirstByItemIdOrderByEndAsc(long itemId);
}
