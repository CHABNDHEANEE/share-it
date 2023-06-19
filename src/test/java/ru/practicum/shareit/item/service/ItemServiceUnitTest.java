package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.ItemBooking;
import ru.practicum.shareit.booking.service.impl.ItemBookingServiceImpl;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.service.impl.CommentServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.request.repo.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceUnitTest {

    @Mock
    private ItemBookingServiceImpl bookingService;
    @Mock
    private CommentServiceImpl commentService;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemServiceImpl itemService;

    Item item1;
    ItemDto item1Dto;
    ItemBooking itemBooking1;
    ItemBooking itemBooking2;
    CommentDto comment1;

    @BeforeEach
    void beforeEach() {
        LocalDateTime currentTime = LocalDateTime.now();

        User user1 = new User(1L, "user1 name", "user1@mail.com");
        User user2 = new User(2L, "user2 name", "user2@mail.com");
        item1 = Item.builder()
                .id(1L)
                .name("item1 name")
                .description("item1 description")
                .owner(user1)
                .available(true)
                .build();
        item1Dto = ItemDto.builder()
                .id(1L)
                .name("item1 name")
                .description("item1 description")
                .owner(user1)
                .available(true)
                .build();
        itemBooking1 = ItemBooking.builder()
                .id(1L)
                .start(currentTime.minusDays(1))
                .end(currentTime.minusHours(1))
                .bookerId(1L)
                .status(BookingStatus.APPROVED)
                .build();
        itemBooking2 = ItemBooking.builder()
                .id(2L)
                .start(currentTime.plusDays(1))
                .end(currentTime.plusDays(1).plusHours(1))
                .bookerId(1L)
                .status(BookingStatus.APPROVED)
                .build();
        comment1 = new CommentDto(1L,"comment1 text", user2.getName(), currentTime);
    }

    @Test
    public void getItem_ForUser_AndExpectLastBooking() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(bookingService.getLastBooking(1L)).thenReturn(itemBooking1);
        when(bookingService.getNextBooking(1L)).thenReturn(itemBooking2);
        when(commentService.getAllCommentsByItemId(1L)).thenReturn(List.of(comment1));

        ItemDto result = itemService.getItem(1L, 1L);

        verify(itemRepository, times(1)).findById(1L);
        verify(bookingService, times(1)).getLastBooking(1L);
        verify(bookingService, times(1)).getNextBooking(1L);
        verify(commentService, times(2)).getAllCommentsByItemId(1L);
        assertItem(item1, result);
    }

    private void assertItem(Item input, ItemDto output) {
        assertThat(input.getId(), is(output.getId()));
        assertThat(input.getName(), is(output.getName()));
        assertThat(input.getDescription(), is(output.getDescription()));
        assertThat(input.isAvailable(), is(output.getAvailable()));
        assertThat(input.getOwner(), is(output.getOwner()));
    }
}
