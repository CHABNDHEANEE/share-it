package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingCondition;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService service;

    @PostMapping
    public BookingDto addBooking(@RequestBody @Valid BookingDto booking, @RequestHeader(value = USER_ID_HEADER) long userId) {
        return service.addBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable long bookingId,
                           @RequestParam boolean approved,
                           @RequestHeader(value = USER_ID_HEADER) long userId) {
        return service.approveBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable long bookingId, @RequestHeader(value = USER_ID_HEADER) long userId) {
        return service.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByUser(@RequestParam(defaultValue = "ALL") BookingCondition state,
                                      @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return service.getAllByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByItems(@RequestParam(defaultValue = "ALL") BookingCondition state,
                                            @RequestHeader(value = USER_ID_HEADER) long userId) {
        return service.getBookingsByItems(userId, state);
    }
}
