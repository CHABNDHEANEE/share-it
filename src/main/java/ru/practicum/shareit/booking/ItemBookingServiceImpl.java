package ru.practicum.shareit.booking;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ItemBookingServiceImpl implements ItemBookingService{
    private final BookingRepository repository;

    @Override
    public Booking getLastBooking(long itemId) {
        return repository.findDistinctFirstByItemIdOrderByEndAsc(itemId);
    }

    @Override
    public Booking getNextBooking(long itemId) {
        return repository.findDistinctFirstByItemIdOrderByStart(itemId);
    }
}
