package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestService requestService;

    @GetMapping
    public List<ItemRequestDto> getAllUserRequests(@RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.getUserRequests(userId);
    }

    @PostMapping
    public ItemRequestDto addRequest(@RequestBody ItemRequestDto itemRequestDto,
                                     @RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.addRequest(itemRequestDto, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllRequests(@RequestParam int from, @RequestParam int size) {
        return requestService.findAllRequests(from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable long requestId) {
        return requestService.getRequestById(requestId);
    }
}
