package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.stats.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Integer> {

    @Query("""
                SELECT new ru.practicum.ewm.dto.ViewStatsDto(e.app, e.uri, COUNT(e.id))
                FROM hits e
                WHERE e.timestamp BETWEEN ?1 AND ?2 AND (?3 IS NULL OR e.uri IN ?3)
                GROUP BY e.app, e.uri
                ORDER BY COUNT(e.id) DESC
            """)
    List<ViewStatsDto> findAllByTimestampBetweenAndUriIn(LocalDateTime start,
                                                         LocalDateTime end,
                                                         List<String> uris);

    @Query("""
            SELECT new ru.practicum.ewm.dto.ViewStatsDto(e.app, e.uri, COUNT(DISTINCT e.ip))
            FROM hits e
            WHERE e.timestamp BETWEEN ?1 AND ?2 AND (?3 IS NULL OR e.uri IN ?3)
            GROUP BY e.app, e.uri
            ORDER BY COUNT(DISTINCT e.ip) DESC
            """)
    List<ViewStatsDto> findAllUniqueIpAndTimestampBetweenAndUriIn(@Param("start") LocalDateTime start,
                                                                  @Param("end") LocalDateTime end,
                                                                  @Param("uris") List<String> uris);
}
