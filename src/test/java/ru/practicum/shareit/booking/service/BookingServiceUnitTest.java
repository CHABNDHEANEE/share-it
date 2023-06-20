package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingCondition;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.exception.ObjectAccessException;
import ru.practicum.shareit.exception.ObjectCreationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceUnitTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking booking;
    private User user;
    private User user2;

    @BeforeEach
    void beforeEach() {
        LocalDateTime currentTime = LocalDateTime.now();

        user = User.builder()
                .id(1L)
                .name("user1 name")
                .email("user1@testmail.com")
                .build();
        user2 = User.builder()
                .id(2L)
                .name("user2 name")
                .email("user2@testmail.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("item name")
                .description("description")
                .available(true)
                .owner(user)
                .build();

        booking = Booking.builder()
                .id(1L)
                .item(item)
                .start(currentTime.plusHours(1))
                .end(currentTime.plusHours(2))
                .booker(user)
                .build();
    }

    @Test
    public void addBooking_AndExpectError() {
        booking.setEnd(LocalDateTime.MIN);

        ObjectCreationException exception = assertThrows(ObjectCreationException.class, () ->
                bookingService.addBooking(BookingMapper.bookingToDto(booking), 1L));

        assertThat(exception.getMessage(), is("End date cannot be before/equal start date"));
    }

    @Test
    public void getBookingTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.getBooking(1L, 1L);

        verify(bookingRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);

        assertBooking(booking, result);
    }

    @Test
    public void getBooking_AndExpectError() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        ObjectAccessException exception = assertThrows(ObjectAccessException.class, () ->
                bookingService.getBooking(1L, 2L));

        assertThat(exception.getMessage(), is("You don't have access to this booking"));
    }

    @Test
    public void getAllByUserTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(1L, PageRequest.of(0, 10)))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllByUser(1L, BookingCondition.ALL, 0, 10);

        assertThat(result.size(), is(1));
        assertBooking(booking, result.get(0));
    }

    @Test
    public void getBookingsByItems() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(1L, PageRequest.of(0, 10)))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getBookingsByItems(1L, BookingCondition.ALL, 0, 10);

        assertThat(result.size(), is(1));
        assertBooking(booking, result.get(0));
    }

    private void assertBooking(Booking expected, BookingDto result) {
        assertThat(expected.getId(), is(result.getId()));
        assertThat(expected.getItem().getId(), is(result.getItemId()));
        assertThat(expected.getStart(), is(result.getStart()));
        assertThat(expected.getEnd(), is(result.getEnd()));
        assertThat(expected.getBooker().getId(), is(result.getBookerId()));
    }
}
