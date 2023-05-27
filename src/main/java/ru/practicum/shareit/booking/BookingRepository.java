package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(long id);

    List<Booking> findAllByItemOwnerId(long id);

    List<Booking> findAllByItemId(long id);

    Optional<Booking> findFirstByItemIdAndEndBeforeOrderByEndDesc(long itemId, LocalDateTime date);

    Optional<Booking> findFirstByItemIdAndStatus(long itemId, BookingStatus status);

    Optional<Booking> findBookingByEndBeforeAndItemIdAndBookerId(LocalDateTime dateTime, long itemId, long bookerId);
}
