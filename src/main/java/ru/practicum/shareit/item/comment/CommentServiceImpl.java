package ru.practicum.shareit.item.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.ItemBookingService;
import ru.practicum.shareit.exception.ObjectAccessException;
import ru.practicum.shareit.exception.ObjectExistenceException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.CommentService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemBookingService bookingService;

    @Override
    public Comment add(long itemId, Comment comment, long userId) {
        if (bookingService.checkBookingCompleted(itemId, userId))
            throw new ObjectAccessException("You don't book this item yet");

        User user = getUserById(userId);
        Item item = getItemById(itemId);

        comment.setItem(item);
        comment.setUser(user);

        return repository.save(comment);
    }

    @Override
    public List<Comment> getCommentsForItem(long itemId) {
        Item item = getItemById(itemId);

        return repository.findAllByItemId(itemId);
    }

    @Override
    public List<Comment> getCommentsForUser(long userId) {
        User user = getUserById(userId);

        return repository.findAllByUserId(userId);
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
