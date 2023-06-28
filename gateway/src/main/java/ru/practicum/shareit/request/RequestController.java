package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.paging.Paging;
import ru.practicum.shareit.paging.PagingParam;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final RequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> getAllUserRequests(@PagingParam({0, 10})Paging paging,
                                                     @RequestHeader(USER_ID_HEADER) long userId) {
        return requestClient.getAllUserRequests(paging, userId);
    }

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestBody ItemRequestDto itemRequestDto,
                                     @RequestHeader(USER_ID_HEADER) long userId) {
        return requestClient.addRequest(itemRequestDto, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllRequests(@PagingParam({0, 10}) Paging paging,
                                                @RequestHeader(USER_ID_HEADER) long userId) {
        return requestClient.findAllRequests(paging, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable long requestId,
                                         @RequestHeader(USER_ID_HEADER) long userId) {
        return requestClient.getRequestById(requestId, userId);
    }
}
