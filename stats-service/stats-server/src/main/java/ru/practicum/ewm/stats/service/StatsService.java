package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.dto.GetStatDto;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.ParamDto;

import java.util.List;

public interface StatsService {
    void hit(HitDto hitDto);

    List<GetStatDto> getStats(ParamDto paramDto);
}
