package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ObjectAccessException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService service;
    private final CommentService commentService;

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId, @RequestHeader(USER_ID_HEADER) long userId) {

        return service.getItem(itemId, userId);
    }

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto item,
                           @RequestHeader(value = USER_ID_HEADER) Long userId) {
        try {
            return service.addItem(item, userId);
        } catch (Exception e) {
            throw new ObjectAccessException("error from contr " + e.getMessage());
        }
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                           @RequestBody ItemDto item,
                           @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return service.updateItem(itemId, item, userId);
    }

    @GetMapping
    public List<ItemDto> getItemsList(@RequestHeader(value = USER_ID_HEADER) Long userId) {
        return service.getItemsList(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return service.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public Comment addComment(@PathVariable long itemId, @RequestBody Comment comment, @RequestHeader long userId) {
        return commentService.add(itemId, comment, userId);
    }
}
