package ru.practicum.ewm.stats.mapper;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.stats.model.Hit;

public class HitMapper {
    public static Hit toHit(EndpointHitDto endpointHitDto) {
        return Hit.builder()
                .app(endpointHitDto.getApp())
                .ip(endpointHitDto.getIp())
                .uri(endpointHitDto.getUri())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }
}
