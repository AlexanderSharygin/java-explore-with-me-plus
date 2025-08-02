package ru.practicum.ewm.stats.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.StatsDto;
import ru.practicum.ewm.stats.mapper.HitMapper;
import ru.practicum.ewm.stats.model.App;
import ru.practicum.ewm.stats.model.Hit;
import ru.practicum.ewm.stats.repository.AppRepository;
import ru.practicum.ewm.stats.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final HitRepository hitRepository;
    private final AppRepository appRepository;

    @Override
    public List<StatsDto> getStats(LocalDateTime startRange, LocalDateTime endRange, List<String> uris, boolean unique) {
        List<StatsDto> stats;
        if (uris == null || uris.isEmpty()) {
            if (!unique) {
                stats = hitRepository.findAllHits(startRange, endRange);
            } else {
                stats = hitRepository.findAllUniqueHits(startRange, endRange);
                stats.forEach(k -> k.setHits(1L));
            }
        } else {
            if (!unique) {
                stats = hitRepository.findHitsByUris(startRange, endRange, uris);
            } else {
                stats = hitRepository.findUniqueHitsByUris(startRange, endRange, uris);
                stats.forEach(k -> k.setHits(1L));
            }
        }

        return stats;
    }

    @Override
    public void hit(HitDto hitDto) {
        hitDto.setTimestamp(LocalDateTime.now());
        Optional<App> existedApp = appRepository.findByName(hitDto.getApp());
        Hit hit = HitMapper.toHit(hitDto);
        if (existedApp.isPresent()) {
            hit.setApp(existedApp.get());
        } else {
            App app = new App();
            app.setName(hitDto.getApp());
            hit.setApp(appRepository.save(app));
        }
        hitRepository.save(hit);
    }
}
