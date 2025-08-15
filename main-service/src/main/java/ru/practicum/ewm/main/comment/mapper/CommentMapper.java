package ru.practicum.ewm.main.comment.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.comment.dto.CommentCreateDto;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.model.Comment;

@NoArgsConstructor
public class CommentMapper {

    public static Comment toEntity(Long authorId, CommentCreateDto dto) {
        Comment c = new Comment();
        c.setEventId(dto.getEventId());
        c.setAuthorId(authorId);
        c.setText(dto.getText());
        return c;
    }

    public static CommentDto toDto(Comment c) {
        return CommentDto.builder()
                .id(c.getId())
                .eventId(c.getEventId())
                .authorId(c.getAuthorId())
                .text(c.getText())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
