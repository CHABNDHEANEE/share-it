package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectAccessException;
import ru.practicum.shareit.exception.ObjectAvailabilityException;
import ru.practicum.shareit.exception.ObjectCreationException;
import ru.practicum.shareit.exception.ObjectExistenceException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Instant;
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
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ObjectExistenceException("Item is not existing"));
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ObjectExistenceException("User doesn't exists"));
        if (!item.isAvailable())
            throw new ObjectAvailabilityException("Item is not available");
        return BookingMapper.bookingToDto(repository.save(BookingMapper.bookingFromDto(bookingDto, item, user)));
    }

    @Override
    public BookingDto approve(long bookingId, boolean status, long userId) {
        BookingDto booking = BookingMapper.bookingToDto(getBookingById(bookingId));
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new ObjectAvailabilityException("Item is not existing"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectExistenceException("User doesn't exists"));

        if (item.getOwner().getId() != userId)
            throw new ObjectAccessException("You don't have access to this booking");
        booking.setStatus(status ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.bookingToDto(repository.save(BookingMapper.bookingFromDto(booking, item, user)));
    }

    @Override
    public BookingDto get(long bookingId, long userId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getItem().getOwner().getId() != userId || booking.getBooker().getId() != userId)
            throw new ObjectAccessException("You don't have access to this booking");
        return BookingMapper.bookingToDto(booking);
    }

    @Override
    public List<BookingDto> getAllByUser(long userId, BookingCondition status) {
        return repository.findAllByBookerId(userId).stream()
                .filter(o -> o.getStatus().equals(status))
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(BookingMapper::bookingToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByItems(long userId, BookingCondition status) {
        return repository.findAllByItemOwnerId(userId).stream()
                .filter(o -> o.getStatus().equals(status))
                .map(BookingMapper::bookingToDto)
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

    private void checkDate(BookingDto booking) {
        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().equals(booking.getEnd()))
            throw new ObjectCreationException("End date cannot be after/equal start date");
    }
}
