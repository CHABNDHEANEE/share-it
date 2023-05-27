package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ItemBookingServiceImpl implements ItemBookingService {
    private final BookingRepository repository;

    @Override
    public BookingDto getLastBooking(long itemId) {
        try {
            return BookingMapper.bookingToDto(repository.findFirstByItemIdAndEndBeforeOrderByEndDesc(itemId, LocalDateTime.now()));
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public BookingDto getNextBooking(long itemId) {
        try {
            return BookingMapper.bookingToDto(repository.findFirstByItemIdAndStatusAndStartAfterOrderByStart(itemId,
                    BookingStatus.APPROVED,
                    LocalDateTime.now()));
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public boolean checkBookingCompleted(long itemId, long userId) {
        return repository.findBookingByEndBeforeAndItemIdAndBookerId(LocalDateTime.now(), itemId, userId).isPresent();
    }
}
