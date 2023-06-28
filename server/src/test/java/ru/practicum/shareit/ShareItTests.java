package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.user.controller.UserController;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ShareItTests {

	@Autowired
	private BookingController bookingController;
	@Autowired
	private ItemController itemController;
	@Autowired
	private ItemRequestController requestController;
	@Autowired
	private UserController userController;

	@Test
	void contextLoads() {
		assertThat(bookingController).isNotNull();
		assertThat(itemController).isNotNull();
		assertThat(requestController).isNotNull();
		assertThat(userController).isNotNull();
	}

}
