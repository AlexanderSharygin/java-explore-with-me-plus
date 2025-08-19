package ru.practicum.ewm.main.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
