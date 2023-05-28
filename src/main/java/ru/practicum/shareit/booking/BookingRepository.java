package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(long id);

    List<Booking> findAllByItemOwnerId(long id);

    List<Booking> findAllByItemId(long id);

    Booking findFirstByItemIdAndStatusAndStartAfterOrderByStart(long itemId, BookingStatus status, LocalDateTime date);

    Optional<Booking> findFirstBookingByEndBeforeAndItemIdAndBookerIdAndStatus(LocalDateTime dateTime, long itemId, long bookerId, BookingStatus status);
}
