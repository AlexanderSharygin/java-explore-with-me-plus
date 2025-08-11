package ru.practicum.ewm.main.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.event.dto.CreateNewEventDto;
import ru.practicum.ewm.main.event.dto.EventDto;
import ru.practicum.ewm.main.event.dto.EventShortDto;
import ru.practicum.ewm.main.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.main.event.service.EventService;
import ru.practicum.ewm.main.request.dto.RequestDto;
import ru.practicum.ewm.main.request.dto.RequestStatusUpdateRequest;
import ru.practicum.ewm.main.request.dto.RequestStatusUpdateResponse;
import ru.practicum.ewm.main.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class EventPrivateController {

    private final EventService eventService;
    private final RequestService requestService;


    @Autowired
    public EventPrivateController(EventService eventService, RequestService requestService) {
        this.eventService = eventService;

        this.requestService = requestService;
    }


    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                             @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(from, size);
        return eventService.getByUserId(userId, paging);
    }


    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@RequestBody @Valid CreateNewEventDto newEventDto, @PathVariable Long userId) {
        return eventService.create(newEventDto, userId);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventDto getUserEvents(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventByUserId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventDto updateEvent(@RequestBody @Valid UpdateEventUserRequest eventDto,
                                @PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.updateByUser(eventDto, userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public RequestStatusUpdateResponse updateRequest(@PathVariable Long userId, @PathVariable Long eventId,
                                                     @RequestBody RequestStatusUpdateRequest request) {
        return requestService.updateRequest(userId, eventId, request);
    }
}
