package ru.practicum.ewm.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.main.event.model.Location;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateNewEventDto {
    @NotBlank
    @Size(min = 20, max = 500)
    private String annotation;
    @NotNull
    private long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @NotNull
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private long participantLimit;
    private Boolean requestModeration;
    @NotBlank
    @Size(min = 1, max = 100)
    private String title;
}
