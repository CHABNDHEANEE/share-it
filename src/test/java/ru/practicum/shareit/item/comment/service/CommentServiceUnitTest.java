package ru.practicum.shareit.item.comment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.service.impl.ItemBookingServiceImpl;
import ru.practicum.shareit.exception.ObjectAvailabilityException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.service.impl.CommentServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceUnitTest {

    @Mock
    private ItemBookingServiceImpl bookingService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private CommentServiceImpl commentService;

    private CommentDto comment;

    @BeforeEach
    void beforeEach() {
        LocalDateTime currentTime = LocalDateTime.now();

        User user = User.builder()
                .id(1L)
                .name("user1 name")
                .email("user1@testmail.com")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("item name")
                .description("description")
                .available(true)
                .owner(user)
                .build();
        comment = CommentDto.builder()
                .id(1L)
                .text("sometext")
                .created(currentTime)
                .build();
    }

    @Test
    public void addComment_AndExpectError() {
        when(bookingService.checkBookingCompleted(1L, 1L)).thenReturn(true);

        ObjectAvailabilityException exception = assertThrows(ObjectAvailabilityException.class, () -> {
            commentService.add(1L, comment, 1L);
        });

        assertThat(exception.getMessage(), is("You don't book this item yet"));
    }
}
