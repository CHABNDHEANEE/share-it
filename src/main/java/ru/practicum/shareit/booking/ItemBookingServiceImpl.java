package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ItemBookingServiceImpl implements ItemBookingService {
    private final BookingRepository repository;

    @Override
    public ItemBooking getLastBooking(long itemId) {
        try {
            return BookingMapper.bookingToItemBooking(repository.findAllByItemId(itemId).stream()
                    .filter(o -> o.getStart().isBefore(LocalDateTime.now()) || o.getStart().isEqual(LocalDateTime.now()))
                    .max(Comparator.comparing(Booking::getEnd)).orElseThrow(NullPointerException::new)
                    );
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public ItemBooking getNextBooking(long itemId) {
        try {
            return BookingMapper.bookingToItemBooking(repository.findFirstByItemIdAndStatusAndStartAfterOrderByStart(itemId,
                    BookingStatus.APPROVED,
                    LocalDateTime.now()));
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public boolean checkBookingCompleted(long itemId, long userId) {
        return repository.findFirstBookingByEndBeforeAndItemIdAndBookerIdAndStatus(LocalDateTime.now(),
                itemId, userId, BookingStatus.APPROVED).isEmpty();
    }
}
