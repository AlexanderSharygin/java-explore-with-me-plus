package ru.practicum.ewm.main.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.comment.dto.CommentCreateDto;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.dto.CommentUpdateDto;
import ru.practicum.ewm.main.comment.service.CommentService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
public class CommentController {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final CommentService commentService;


    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@RequestHeader(USER_HEADER) Long userId,
                             @Valid @RequestBody CommentCreateDto dto) {
        return commentService.create(userId, dto);
    }

    @GetMapping("/events/{eventId}/comments")
    public Page<CommentDto> listByEvent(@PathVariable Long eventId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return commentService.listByEvent(eventId, from, size);
    }

    @DeleteMapping("/comments/{id}")
    public void delete(@RequestHeader(USER_HEADER) Long userId,
                       @PathVariable Long id) {
        commentService.delete(id, userId);
    }

    @GetMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentById(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CommentDto> getAllCommentsByUser(@PathVariable Long userId,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getAllCommentsByUser(userId, from, size);
    }

    @GetMapping("events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<CommentDto> getAllCommentsByEvent(@PathVariable Long eventId,
                                                             @PathVariable Long userId,
                                                             @RequestParam(defaultValue = "0") Integer from,
                                                             @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getAllCommentsByUserAndEvent(userId, eventId, from, size);
    }
}