package ru.practicum.shareit.booking;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemBookingServiceImpl implements ItemBookingService{
    private final BookingRepository repository;

    @Override
    public Optional<Booking> getLastBooking(long itemId) {
        return repository.findFirstByItemIdAndEndBeforeOrderByEndDesc(itemId, LocalDateTime.now());
    }

    @Override
    public Optional<Booking> getNextBooking(long itemId) {
        return repository.findFirstByItemIdAndStatus(itemId, BookingStatus.APPROVED);
    }

    @Override
    public boolean checkBookingCompleted(long itemId, long userId) {
        return repository.findBookingByEndBeforeAndItemIdAndBookerId(LocalDateTime.now(), itemId, userId).isPresent();
    }
}
