package shareit.item.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.service.ItemBookingService;
import ru.practicum.shareit.exception.ObjectAvailabilityException;
import ru.practicum.shareit.exception.ObjectExistenceException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repo.CommentRepository;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemBookingService bookingService;

    @Override
    public CommentDto add(long itemId, CommentDto commentDto, long userId) {
        if (bookingService.checkBookingCompleted(itemId, userId)) {
            throw new ObjectAvailabilityException("You don't book this item yet");
        }

        User user = getUserById(userId);
        Item item = getItemById(itemId);
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = CommentMapper.fromDto(commentDto, item, user);

        comment.setItem(item);
        comment.setUser(user);

        return CommentMapper.toDto(repository.save(comment));
    }

    @Override
    public List<CommentDto> getAllCommentsByItemId(long itemId) {
        return repository.findCommentsByItem_Id(itemId).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
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
