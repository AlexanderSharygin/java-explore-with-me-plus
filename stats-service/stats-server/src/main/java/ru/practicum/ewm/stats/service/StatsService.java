package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ParamDto;

import java.util.List;

public interface StatsService {
    void hit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStats(ParamDto paramDto);
}
