package ru.practicum.ewm.stats.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ParamDto;
import ru.practicum.ewm.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    void addHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Поступил запрос POST /hit на создание hit {}", endpointHitDto);
        statsService.hit(endpointHitDto);
        log.info("Запрос POST /hit успешно обработан");
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    Collection<ViewStatsDto> getStatistics(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(required = false, defaultValue = "false") boolean unique) {
        log.info("Поступил запрос GET /stats на получение статистики {}", new ParamDto(start, end, uris, unique));
        List<ViewStatsDto> viewStats = statsService.getStats(new ParamDto(start, end, uris, unique));
        log.info("Запрос GET /stats успешно обработан {}", viewStats);
        return viewStats;
    }
}
