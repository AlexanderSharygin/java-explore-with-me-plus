package ru.practicum.ewm.stats.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.stats.model.Hit;

@NoArgsConstructor
public class HitMapper {
    public static HitDto toHitDto(Hit hit) {
        return new HitDto(
                hit.getId(),
                hit.getApp().getName(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }

    public static Hit toHit(HitDto hitDto) {
        return new Hit(hitDto.getId(),
                hitDto.getIp(),
                hitDto.getUri(),
                hitDto.getTimestamp(),
                null
        );
    }
}