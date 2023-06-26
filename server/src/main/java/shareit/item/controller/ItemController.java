package shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.paging.Paging;
import ru.practicum.shareit.paging.PagingParam;

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
    public ItemDto getItem(@PathVariable Long itemId,
                           @RequestHeader(USER_ID_HEADER) long userId) {
        return service.getItem(itemId, userId);
    }

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto item,
                           @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return service.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody ItemDto item,
                              @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return service.updateItem(itemId, item, userId);
    }

    @GetMapping
    public List<ItemDto> getItemsList(@RequestHeader(value = USER_ID_HEADER) Long userId,
                                      @PagingParam({0, 10})Paging paging) {
        return service.getItemsList(userId, paging.getFrom(), paging.getSize());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text,
                                     @PagingParam({0, 10})Paging paging) {
        return service.searchItem(text, paging.getFrom(), paging.getSize());
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable long itemId,
                                 @Valid @RequestBody CommentDto comment,
                                 @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return commentService.add(itemId, comment, userId);
    }
}
