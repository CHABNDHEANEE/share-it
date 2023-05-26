package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService service;

    @PostMapping
    public void add(@RequestBody Booking booking, @RequestHeader(value = USER_ID_HEADER) long userId) {
        service.add(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking approve(@PathVariable long bookingId,
                           @RequestParam boolean status,
                           @RequestHeader(value = USER_ID_HEADER) long userId) {
        return service.approve(bookingId, status, userId);
    }

    @GetMapping("/{bookingId}")
    public Booking get(long bookingId, @RequestHeader(value = USER_ID_HEADER) long userId) {
        return service.get(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getAllByUser(@RequestParam(defaultValue = "ALL") BookingCondition state,
                                      @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return service.getAllByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getBookingsByItems(@RequestParam(defaultValue = "ALL") BookingCondition state,
                                            @RequestHeader(value = USER_ID_HEADER) long userId) {
        return service.getBookingsByItems(userId, state);
    }
}
