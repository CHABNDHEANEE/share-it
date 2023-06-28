package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.paging.Paging;
import ru.practicum.shareit.paging.PagingParam;

import javax.validation.Valid;

@Controller
@RequestMapping("/items")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId,
                                          @RequestHeader(USER_ID_HEADER) long userId) {
        return itemClient.getItem(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestBody @Valid ItemDto item,
                                          @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return itemClient.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId,
                                             @RequestBody @Valid ItemDto item,
                                             @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return itemClient.updateItem(itemId, item, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsList(@RequestHeader(value = USER_ID_HEADER) Long userId,
                                               @PagingParam({0, 10})Paging paging) {
        return itemClient.getItemsList(userId, paging);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                              @RequestHeader(value = USER_ID_HEADER) Long userId,
                                              @PagingParam({0, 10}) Paging paging) {
        return itemClient.searchItem(text, userId, paging);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable long itemId,
                                             @RequestBody @Valid CommentDto comment,
                                             @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return itemClient.addComment(itemId, comment, userId);
    }
}
