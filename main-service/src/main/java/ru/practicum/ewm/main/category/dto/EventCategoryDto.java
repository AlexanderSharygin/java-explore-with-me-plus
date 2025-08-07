package ru.practicum.ewm.main.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EventCategoryDto {
    private Long id;
    @NotBlank
    private String name;

}
