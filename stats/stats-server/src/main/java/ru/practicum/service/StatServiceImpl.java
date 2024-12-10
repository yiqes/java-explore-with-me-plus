package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatServiceImpl implements StatService {

    private final EndpointHitRepository endpointHitRepository;

    public StatServiceImpl(EndpointHitRepository endpointHitRepository) {
        this.endpointHitRepository = endpointHitRepository;
    }

    @Override
    public void hit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();

        endpointHitRepository.save(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStatsDto> stats = new ArrayList<>();
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                stats = endpointHitRepository.findUniqueStatsBetween(start, end);
            } else {
                stats = endpointHitRepository.findStatsBetween(start, end);
            }
        } else {
            for (String uri : uris) {
                if (unique) {
                    int hits = endpointHitRepository.countDistinctIpByUriAndTimestampBetween(uri, start, end);
                    stats.add(new ViewStatsDto(null, uri, hits));
                } else {
                    int hits = endpointHitRepository.countByUriAndTimestampBetween(uri, start, end);
                    stats.add(new ViewStatsDto(null, uri, hits));
                }
            }
        }
        return stats;
    }
}
