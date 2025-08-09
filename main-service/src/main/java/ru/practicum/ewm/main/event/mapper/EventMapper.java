package ru.practicum.ewm.main.event.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.category.dto.EventCategoryDto;
import ru.practicum.ewm.main.category.model.EventCategory;
import ru.practicum.ewm.main.event.dto.CreateNewEventDto;
import ru.practicum.ewm.main.event.dto.EventDto;
import ru.practicum.ewm.main.event.dto.EventShortDto;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.event.model.EventState;
import ru.practicum.ewm.main.user.dto.UserShortDto;
import ru.practicum.ewm.main.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@NoArgsConstructor
public class EventMapper {


    public static Event fromCreateNewEventDtoToEvent(CreateNewEventDto newEventDto, User owner, EventCategory category) {
        return new Event(null,
                newEventDto.getTitle(),
                newEventDto.getAnnotation(),
                newEventDto.getDescription(),
                category,
                LocalDateTime.now().toInstant(ZoneOffset.UTC),
                newEventDto.getEventDate().toInstant(ZoneOffset.UTC),
                owner, newEventDto.getLocation(),
                newEventDto.getPaid(), newEventDto.getParticipantLimit(),
                null,
                newEventDto.getRequestModeration(), EventState.PENDING);
    }

    public static EventDto fromEventToEventDto(Event event, EventCategoryDto eventCategoryDto, UserShortDto owner,
                                               Long confirmedRequests, Integer views) {
        EventDto eventDto = new EventDto(event.getId(),
                event.getAnnotation(),
                eventCategoryDto,
                confirmedRequests,
                LocalDateTime.ofInstant(event.getCreatedOn(), ZoneId.of("UTC")),
                event.getDescription(),
                LocalDateTime.ofInstant(event.getEventDateTime(), ZoneId.of("UTC")),
                owner,
                event.getLocation(), event.isPaid(), event.getParticipantLimit(),
                null,
                event.isModerated(), event.getState(), event.getTitle(), views
        );
        if (event.getPublishedOn() != null) {
            eventDto.setPublishedOn(LocalDateTime.ofInstant(event.getPublishedOn(), ZoneId.of("UTC")));
        }
        return eventDto;
    }

    public static EventShortDto fromEventToEventShortDto(Event event, EventCategoryDto eventCategoryDto, UserShortDto owner,
                                                         Long confirmedRequests, Integer views) {
        return new EventShortDto(event.getId(), event.getAnnotation(), eventCategoryDto, confirmedRequests,
                LocalDateTime.ofInstant(event.getEventDateTime(), ZoneId.of("UTC")), owner,
                event.isPaid(), event.getTitle(), views);
    }
}
