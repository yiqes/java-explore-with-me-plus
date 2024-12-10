package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Integer> {

    @Query("select new ru.practicum.dto.ViewStatsDto(e.app, e.uri, count(e)) " +
            "from EndpointHit e " +
            "where e.timestamp between :start and :end " +
            "group by e.app, e.uri")
    List<ViewStatsDto> findStatsBetween(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.ViewStats(e.app, e.uri, count(distinct e.ip)) " +
            "from EndpointHit e " +
            "where e.timestamp between :start and :end " +
            "group by e.app, e.uri")
    List<ViewStatsDto> findUniqueStatsBetween(LocalDateTime start, LocalDateTime end);

    Integer countByUriAndTimestampBetween(String uri, LocalDateTime start, LocalDateTime end);
    Integer countDistinctIpByUriAndTimestampBetween(String uri, LocalDateTime start, LocalDateTime end);
}
