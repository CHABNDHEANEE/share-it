package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.paging.Paging;
import ru.practicum.shareit.paging.PagingParam;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private static final String USER_ID_HEADER = "X-Sharer-User-Id";
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> addBooking(@RequestBody @Valid BookingDto booking,
											 @RequestHeader(value = USER_ID_HEADER) long userId) {
		return bookingClient.addBooking(booking, userId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBooking(@PathVariable long bookingId,
												 @RequestParam boolean approved,
												 @RequestHeader(value = USER_ID_HEADER) long userId) {
		return bookingClient.approveBooking(bookingId, approved, userId);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader(USER_ID_HEADER) long userId,
											 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBookingById(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getBookingsByUser(@RequestHeader(USER_ID_HEADER) long userId,
													@RequestParam(defaultValue = "ALL") BookingState state,
													@PagingParam({0, 10})Paging paging) {

		return bookingClient.getBookingsByUser(userId, state, paging.getFrom(), paging.getSize());
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingByItems(@RequestParam(defaultValue = "ALL") BookingState state,
													@RequestHeader(value = USER_ID_HEADER) long userId,
													@PagingParam({0, 10})Paging paging) {
		return bookingClient.getBookingByItems(state, userId, paging.getSize(), paging.getFrom());
	}
}