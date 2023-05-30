package ru.practicum.shareit.booking.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.booking.model.ItemBooking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.ItemBookingService;

import java.time.LocalDateTime;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ItemBookingServiceImpl implements ItemBookingService {
    private final BookingRepository repository;

    @Override
    public ItemBooking getLastBooking(long itemId) {

        final LocalDateTime CURRENT_TIME = LocalDateTime.now();
        final Booking lastBooking = repository.findAllByItemId(itemId).stream()
                .filter(o -> o.getStart().isBefore(CURRENT_TIME) || o.getStart().isEqual(CURRENT_TIME))
                .max(Comparator.comparing(Booking::getEnd)).orElse(null);

        return lastBooking == null ? null : BookingMapper.bookingToItemBooking(lastBooking);
    }

    @Override
    public ItemBooking getNextBooking(long itemId) {
        try {
            final LocalDateTime CURRENT_TIME = LocalDateTime.now();
            final Booking nextBooking = repository.findFirstByItemIdAndStatusAndStartAfterOrderByStart(itemId,
                    BookingStatus.APPROVED,
                    CURRENT_TIME);

            return BookingMapper.bookingToItemBooking(nextBooking);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public boolean checkBookingCompleted(long itemId, long userId) {
        final LocalDateTime CURRENT_TIME = LocalDateTime.now();
        return repository.findFirstBookingByEndBeforeAndItemIdAndBookerIdAndStatus(CURRENT_TIME,
                itemId, userId, BookingStatus.APPROVED).isEmpty();
    }
}
