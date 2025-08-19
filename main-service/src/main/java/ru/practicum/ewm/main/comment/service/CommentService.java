package ru.practicum.ewm.main.comment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.comment.dto.CommentCreateDto;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.mapper.CommentMapper;
import ru.practicum.ewm.main.comment.model.Comment;
import ru.practicum.ewm.main.comment.repository.CommentRepository;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.event.model.EventState;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.exception.model.BadRequestException;
import ru.practicum.ewm.main.exception.model.ConflictException;
import ru.practicum.ewm.main.exception.model.ForbiddenException;
import ru.practicum.ewm.main.exception.model.NotFoundException;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    public CommentService(CommentRepository commentRepository,
                          EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
    }

    public CommentDto getCommentById(Long commentId) {
        log.info("Get comment with id={}", commentId);
        return CommentMapper.toDto(commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Comment with id=%d was not found", commentId))));
    }

    public Collection<CommentDto> getAllCommentsByUser(Long userId, Integer from, Integer size) {
        log.info("Get all comments for user id={}", userId);
        return commentRepository.findAllByAuthorIdOrderByPublishedOnDesc(userId, createPageable(from, size))
                .stream()
                .map(CommentMapper::toDto)
                .toList();
    }

    public Collection<CommentDto> getAllCommentsByUserAndEvent(Long userId, Long eventId, Integer from, Integer size) {
        log.info("Get all comments for event id={} and user id={}", eventId, userId);
        return commentRepository.findAllByAuthorIdAndEventIdOrderByPublishedOnDesc(userId, eventId, createPageable(from, size))
                .stream()
                .map(CommentMapper::toDto)
                .toList();
    }

    public CommentDto create(Long userId, CommentCreateDto dto) {
        if (userId == null) {
            throw new BadRequestException("X-Sharer-User-Id is required");
        }
        if (dto == null) {
            throw new BadRequestException("Request body is required");
        }
        if (dto.getEventId() == null) {
            throw new BadRequestException("eventId is required");
        }
        if (dto.getText() == null || dto.getText().isBlank()) {
            throw new BadRequestException("text is required");
        }

        Optional<Event> optionalEvent = eventRepository.findById(dto.getEventId());
        Event event = optionalEvent.orElseThrow(
                () -> new NotFoundException("Event not found: " + dto.getEventId())
        );

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Event is not published");
        }

        Comment toSave = CommentMapper.toEntity(userId, dto);
        Comment saved = commentRepository.save(toSave);
        CommentDto result = CommentMapper.toDto(saved);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<CommentDto> listByEvent(Long eventId, int from, int size) {
        if (eventId == null) {
            throw new BadRequestException("eventId is required");
        }
        if (from < 0) {
            throw new BadRequestException("'from' must be >= 0");
        }
        if (size <= 0) {
            throw new BadRequestException("'size' must be > 0");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));

        int page = from / size;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Comment> commentsPage = commentRepository.findAllByEventId(event.getId(), pageRequest);
        Page<CommentDto> result = commentsPage.map(CommentMapper::toDto);
        return result;
    }

    public void delete(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found: " + commentId));

        if (!userId.equals(comment.getAuthorId())) {
            throw new ForbiddenException("You are not the author of this comment");
        }
        commentRepository.deleteById(commentId);
    }

    private Pageable createPageable(Integer from, Integer size) {
        int pageNumber = from / size;
        return PageRequest.of(pageNumber, size);
    }
}
