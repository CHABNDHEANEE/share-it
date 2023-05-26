package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectAccessException;
import ru.practicum.shareit.exception.ObjectExistenceException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final UserRepository userRepository;

    @Override
    public void add(Booking booking, long userId) {
        booking.setBooker(getUserById(userId));
        repository.save(booking);
    }

    @Override
    public Booking approve(long bookingId, boolean status, long userId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getItem().getOwner().getId() != userId)
            throw new ObjectAccessException("You don't have access to this booking");
        booking.setStatus(status ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return repository.save(booking);
    }

    @Override
    public Booking get(long bookingId, long userId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getItem().getOwner().getId() != userId || booking.getBooker().getId() != userId)
            throw new ObjectAccessException("You don't have access to this booking");
        return booking;
    }

    @Override
    public List<Booking> getAllByUser(long userId, BookingCondition status) {
        Stream<Booking> bookingStream = filterStreamByCondition(repository.findAllByBookerId(userId).stream(), status);

        return bookingStream.sorted(Comparator.comparing(Booking::getStart).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getBookingsByItems(long userId, BookingCondition status) {
        return filterStreamByCondition(repository.findAllByItemOwnerId(userId).stream(), status)
                .collect(Collectors.toList());
    }

    private Booking getBookingById(long id) {
        Optional<Booking> bookingOptional = repository.findById(id);
        if (bookingOptional.isEmpty())
            throw new ObjectExistenceException("Item doesn't exists");
        return bookingOptional.get();
    }

    private User getUserById(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty())
            throw new ObjectExistenceException("This user doesn't exists");
        return userOptional.get();
    }

    private Stream<Booking> filterStreamByCondition(Stream<Booking> stream, BookingCondition cond) {
        switch (cond) {
            case PAST:
                return stream.filter(o -> o.getEnd().toInstant().isBefore(Instant.now()) &&
                        o.getStatus().equals(BookingStatus.APPROVED));
            case FUTURE:
                return stream.filter(o -> o.getStatus().equals(BookingStatus.APPROVED) &&
                        o.getStart().toInstant().isAfter(Instant.now()));
            case CURRENT:
                return stream.filter(o -> o.getStatus().equals(BookingStatus.APPROVED) &&
                        o.getStart().toInstant().isAfter(Instant.now()) &&
                        o.getEnd().toInstant().isBefore(Instant.now()));
            case WAITING:
                return stream.filter(o -> o.getStatus().equals(BookingStatus.WAITING));
            case REJECTED:
                return stream.filter(o -> o.getStatus().equals(BookingStatus.REJECTED));
            default:
                return stream;
        }
    }
}
