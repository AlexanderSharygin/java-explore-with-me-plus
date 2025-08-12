package ru.practicum.ewm.main.compilation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;


import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CompilationRequestDto {
    private Set<Long> events;
    private Boolean pinned;
    @Size(max = 50)
    private String title;
}
