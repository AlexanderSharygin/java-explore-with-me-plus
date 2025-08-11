package ru.practicum.ewm.main.compilation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CompilationRequestDto {
    private Set<Long> events;
    @NotNull
    private Boolean pinned;
    private String title;
}
