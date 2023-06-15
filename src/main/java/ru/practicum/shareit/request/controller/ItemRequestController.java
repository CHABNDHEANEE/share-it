package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.paging.Paging;
import ru.practicum.shareit.paging.PagingParam;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestService requestService;

    @GetMapping
    public List<ItemRequestDto> getAllUserRequests(@PagingParam({0, 10})Paging paging,
                                                   @RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.getUserRequests(userId, paging.getSize(), paging.getFrom());
    }

    @PostMapping
    public ItemRequestDto addRequest(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                     @RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.addRequest(itemRequestDto, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllRequests(@PagingParam({0, 10})Paging paging,
                                                @RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.findAllRequests(paging.getFrom(), paging.getSize(), userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable long requestId,
                                         @RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.getRequestById(requestId, userId);
    }
}
