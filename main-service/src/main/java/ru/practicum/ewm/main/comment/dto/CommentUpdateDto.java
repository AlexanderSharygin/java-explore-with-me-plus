package ru.practicum.ewm.main.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDto {
    @NotNull(message = "Event id is required")
    Long eventId;

    @NotBlank(message = "Text is required")
    String text;

    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();
}
