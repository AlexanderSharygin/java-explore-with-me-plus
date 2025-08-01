package ru.practicum.ewm.stats.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ParamDto;
import ru.practicum.ewm.stats.mapper.HitMapper;
import ru.practicum.ewm.stats.repository.StatsRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public void hit(EndpointHitDto endpointHitDto) {
        statsRepository.save(HitMapper.toHit(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> getStats(ParamDto paramDto) {
        List<ViewStatsDto> viewStatsList;
        if (paramDto.getUnique()) {
            viewStatsList = statsRepository.findAllUniqueIpAndTimestampBetweenAndUriIn(
                    paramDto.getStart(),
                    paramDto.getEnd(),
                    paramDto.getUris());
        } else {
            viewStatsList = statsRepository.findAllByTimestampBetweenAndUriIn(
                    paramDto.getStart(),
                    paramDto.getEnd(),
                    paramDto.getUris());
        }
        return viewStatsList;
    }

}
