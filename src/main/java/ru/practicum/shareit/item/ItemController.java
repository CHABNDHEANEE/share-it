package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        return service.getItem(itemId);
    }

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto item,
                           @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public void updateItem(@PathVariable Long itemId,
                           @RequestBody ItemDto item,
                           @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        service.updateItem(itemId, item, userId);
    }

    @GetMapping
    public List<ItemDto> getItemsList(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.getItemsList(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return service.searchItem(text);
    }
}
