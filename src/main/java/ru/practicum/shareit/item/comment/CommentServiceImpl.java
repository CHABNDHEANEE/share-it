package ru.practicum.shareit.item.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.ItemBookingService;
import ru.practicum.shareit.exception.ObjectAccessException;
import ru.practicum.shareit.exception.ObjectExistenceException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemBookingService bookingService;

    @Override
    public CommentDto add(long itemId, CommentDto commentDto, long userId) {
        if (bookingService.checkBookingCompleted(itemId, userId))
            throw new ObjectAccessException("You don't book this item yet");

        User user = getUserById(userId);
        Item item = getItemById(itemId);
        Comment comment = CommentMapper.fromDto(commentDto, item, user);

        comment.setItem(item);
        comment.setUser(user);

        return CommentMapper.toDto(repository.save(comment));
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectExistenceException("User doesn't exists"));
    }

    private Item getItemById(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectExistenceException("Item doesn't exists"));
    }
}
