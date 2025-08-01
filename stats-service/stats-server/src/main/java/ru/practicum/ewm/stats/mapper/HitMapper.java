package ru.practicum.ewm.stats.mapper;

import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.stats.model.Hit;

public class HitMapper {
    public static Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .app(hitDto.getApp())
                .ip(hitDto.getIp())
                .uri(hitDto.getUri())
                .timestamp(hitDto.getTimestamp())
                .build();
    }
}
