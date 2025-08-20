package ru.practicum.ewm.main.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MergeCommentRequest {

    @NotNull
    private Long eventId;

    @NotBlank
    private String text;

    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();
}