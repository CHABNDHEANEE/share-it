package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    final BookingRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto add(BookingDto bookingDto, long userId) {
        checkDate(bookingDto);
        Item item = getItemById(bookingDto.getItemId());
        User user = getUserById(userId);

//        if (repository.findAllByItemId(item.getId()).stream().anyMatch(o -> bookingDto.getStart().isAfter(o.getStart()) && bookingDto.getStart().isBefore(o.getEnd()) ||
//                bookingDto.getEnd().isAfter(o.getStart()) && bookingDto.getEnd().isBefore(o.getEnd())))
//            throw new ObjectAvailabilityException("Time is busy");

        if (!item.isAvailable())
            throw new ObjectAvailabilityException("Item is not available");
        if (item.getOwner().equals(user))
            throw new ObjectAccessException("You can't booking your item");
        return BookingMapper.bookingToDto(repository.save(BookingMapper.bookingFromDto(bookingDto, item, user)));
    }

    @Override
    public BookingDto approve(long bookingId, boolean status, long userId) {
        BookingDto booking = BookingMapper.bookingToDto(getBookingById(bookingId));
        Item item = getItemById(booking.getItemId());

        if (item.getOwner().getId() != userId)
            throw new ObjectAccessException("You don't have access to this booking");
        if (booking.getStatus().equals(BookingStatus.APPROVED) || booking.getStatus().equals(BookingStatus.REJECTED))
            throw new ObjectAvailabilityException("Booking already " + booking.getStatus());

        booking.setStatus(status ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.bookingToDto(repository.save(BookingMapper.bookingFromDto(booking, item, booking.getBooker())));
    }

    @Override
    public BookingDto get(long bookingId, long userId) {
        Booking booking = getBookingById(bookingId);
        User user = getUserById(userId);

        if (booking.getItem().getOwner().getId() != user.getId() && booking.getBooker().getId() != user.getId())
            throw new ObjectAccessException("You don't have access to this booking");

        return BookingMapper.bookingToDto(booking);
    }

    @Override
    public List<BookingDto> getAllByUser(long userId, BookingCondition status) {
        User user = getUserById(userId);

        return repository.findAllByBookerId(user.getId()).stream()
                .filter(o -> filterByCondition(o, status))
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(BookingMapper::bookingToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByItems(long userId, BookingCondition status) {
        User user = getUserById(userId);

        return repository.findAllByItemOwnerId(user.getId()).stream()
                .filter(o -> filterByCondition(o, status))
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(BookingMapper::bookingToDto)
                .collect(Collectors.toList());
    }

    private boolean filterByCondition(Booking booking, BookingCondition cond) {
        switch (cond) {
            case REJECTED:
                return booking.getStatus().equals(BookingStatus.REJECTED);
            case PAST:
                return booking.getEnd().isBefore(LocalDateTime.now());
            case CURRENT:
                return booking.getStart().isBefore(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now());
            case FUTURE:
                return booking.getStart().isAfter(LocalDateTime.now()) ||
                        booking.getStart().equals(LocalDateTime.now());
            case WAITING:
                return booking.getStatus().equals(BookingStatus.WAITING);
            default:
                return true;
        }
    }

    private Booking getBookingById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectExistenceException("Booking doesn't exists"));
    }

    private User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectExistenceException("User doesn't exists"));
    }

    private Item getItemById(long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ObjectExistenceException("Item is not existing"));
    }

    private void checkDate(BookingDto booking) {
        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().equals(booking.getEnd()))
            throw new ObjectCreationException("End date cannot be after/equal start date");
    }
}
