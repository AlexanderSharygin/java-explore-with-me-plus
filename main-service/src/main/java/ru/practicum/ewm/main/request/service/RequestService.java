package ru.practicum.ewm.main.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.event.service.EventService;
import ru.practicum.ewm.main.exception.model.ConflictException;
import ru.practicum.ewm.main.exception.model.NotFoundException;
import ru.practicum.ewm.main.request.dto.RequestStatusUpdateRequest;
import ru.practicum.ewm.main.request.dto.RequestStatusUpdateResponse;
import ru.practicum.ewm.main.request.dto.RequestDto;
import ru.practicum.ewm.main.request.dto.RequestMapper;
import ru.practicum.ewm.main.request.model.ParticipationRequest;
import ru.practicum.ewm.main.request.model.RequestStatus;
import ru.practicum.ewm.main.request.repository.RequestRepository;
import ru.practicum.ewm.main.user.model.User;
import ru.practicum.ewm.main.user.repository.UserRepository;
import ru.practicum.ewm.main.user.service.UserService;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EventService eventService;
    private final EventRepository eventRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository,
                          UserRepository userRepository, UserService userService, EventService eventService,
                          EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }

    public List<RequestDto> getEventRequests(Long userId, Long eventId) {
        userService.getUserIfExist(userId);
        Event event = eventService.getEventIfExist(eventId);
        if (!Objects.equals(event.getOwner().getId(), userId)) {
            throw new NotFoundException("User c id " + userId + " не хозяин для события " + eventId);
        }
        List<ParticipationRequest> result = requestRepository.findByEvent(event);

        return result.stream()
                .map(RequestMapper::fromRequestTpRequestDto)
                .collect(Collectors.toList());
    }

    public RequestStatusUpdateResponse updateRequest(Long userId, Long eventId,
                                                     RequestStatusUpdateRequest request) {

        userService.getUserIfExist(userId);
        Event event = eventService.getEventIfExist(eventId);
        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(request.getRequestIds());
        Set<RequestDto> confirmed = new HashSet<>();
        Set<RequestDto> rejected = new HashSet<>();
        RequestStatusUpdateResponse result = new RequestStatusUpdateResponse(confirmed, rejected);
        List<ParticipationRequest> pendingRequests = requests.stream()
                .filter(request1 -> request1.getStatus().equals(RequestStatus.PENDING))
                .toList();

        if (pendingRequests.isEmpty()) {
            throw new ConflictException("Запрос не найден");
        }
        long confirmedRequestsCount = requestRepository.findAllByEventAndStatus(event, RequestStatus.CONFIRMED).size();
        if (!event.getIsModerated() || event.getParticipantLimit() == 0) {
            requests.forEach(req -> req.setStatus(RequestStatus.CONFIRMED));
            result.getConfirmedRequests().addAll(requests.stream()
                    .map(RequestMapper::fromRequestTpRequestDto)
                    .toList());
            requestRepository.saveAll(requests);

            return result;
        }
        if ((confirmedRequestsCount + request.getRequestIds().size()) > event.getParticipantLimit()) {
            throw new ConflictException("Для события с id " + eventId + " достигнут лимит участников");
        }

        if ((confirmedRequestsCount + request.getRequestIds().size()) == event.getParticipantLimit() &&
                request.getStatus().equals(RequestStatus.CONFIRMED)) {
            requests.forEach(req -> req.setStatus(RequestStatus.REJECTED));
            confirmed.addAll(requests.stream()
                    .map(RequestMapper::fromRequestTpRequestDto)
                    .toList());
            requestRepository.saveAll(requests);
            result.setConfirmedRequests(confirmed);

            List<ParticipationRequest> otherPendingRequests = requestRepository
                    .findAllByEventAndStatus(event, RequestStatus.PENDING);
            otherPendingRequests.forEach(req -> req.setStatus(RequestStatus.REJECTED));
            requestRepository.saveAll(otherPendingRequests);
            rejected.addAll(otherPendingRequests.stream().map(RequestMapper::fromRequestTpRequestDto).toList());
            result.setRejectedRequests(rejected);
            return result;
        }

        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            requests.forEach(req -> req.setStatus(RequestStatus.CONFIRMED));
            confirmed.addAll(requests.stream().map(RequestMapper::fromRequestTpRequestDto).toList());
            requestRepository.saveAll(requests);
            result.setConfirmedRequests(confirmed);
        } else if (request.getStatus().equals(RequestStatus.REJECTED)) {
            requests.forEach(req -> req.setStatus(RequestStatus.REJECTED));
            rejected.addAll(requests.stream().map(RequestMapper::fromRequestTpRequestDto).toList());
            requestRepository.saveAll(requests);
            result.setRejectedRequests(rejected);
        }

        return result;
    }

    public List<RequestDto> getByUserId(Long userId) {
        List<ParticipationRequest> result = requestRepository.findAllByRequester(userService.getUserIfExist(userId));

        return result.stream()
                .map(RequestMapper::fromRequestTpRequestDto)
                .collect(Collectors.toList());
    }

    public RequestDto create(Long userId, Long eventId) {
        User user = userService.getUserIfExist(userId);
        Event event = eventService.getEventIfExist(eventId);
        if (Objects.equals(user.getId(), event.getOwner().getId())) {
            throw new ConflictException("User c id " + userId + " не хозяин для события " + eventId);
        }
        if (event.getPublishedOn() == null) {
            throw new ConflictException("Событие с id " + eventId + " еще не опубликовано");
        }

        long confirmedRequestsCount = requestRepository.findAllByEventAndStatus(event, RequestStatus.CONFIRMED).size();
        if (confirmedRequestsCount == event.getParticipantLimit() && event.getParticipantLimit()>0) {
            throw new ConflictException(
                    "Лимит участников для события " + event.getId() + " превышен");
        }
        if (!requestRepository.findAllByEventAndRequester(event, user).isEmpty()) {
            throw new ConflictException("Запрос на участие в событии " + event.getId() +
                    " уже существует для пользователя с id " + user.getId());
        }
        ParticipationRequest request = new ParticipationRequest(null, LocalDateTime.now(), event, user,
                RequestStatus.PENDING);
        if (!event.getIsModerated() ) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        if (event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        ParticipationRequest result = requestRepository.save(request);

        return RequestMapper.fromRequestTpRequestDto(result);
    }

    public RequestDto cancelRequestByUser(Long userId, Long requestId) {
        User user = userService.getUserIfExist(userId);
        ParticipationRequest request = requestRepository
                .findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " not exists in the DB"));
        if (!Objects.equals(request.getRequester().getId(), user.getId())) {
            throw new ConflictException("User with id" + userId + " is not owner for request with id" + requestId);
        }
        request.setStatus(RequestStatus.CANCELED);
        ParticipationRequest result = requestRepository.save(request);

        return RequestMapper.fromRequestTpRequestDto(result);
    }
}
