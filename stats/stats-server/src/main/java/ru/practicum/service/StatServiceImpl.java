package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.*;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {

    private final EndpointHitRepository endpointHitRepository;
    private final EndpointHitMapper mapper;

    @Override
    public EndpointHitResponseDto saveInfo(EndpointHitSaveRequestDto endpointHitSaveRequestDto) {
        EndpointHit endpointHit = mapper.toEndpointHit(endpointHitSaveRequestDto);
        endpointHit = endpointHitRepository.save(endpointHit);
        return mapper.toResponseDto(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        List<ViewStatsDto> stats;

        if (unique) {
            stats = uris.isEmpty() ?
                    endpointHitRepository.findStatAllWithUniqueIp(start, end) :
                    endpointHitRepository.findStatWithUniqueIp(start, end, uris);
        } else {
            stats = uris.isEmpty() ?
                    endpointHitRepository.findStatAllWithoutUniqueIp(start, end) :
                    endpointHitRepository.findStatWithoutUniqueIp(start, end, uris);
        }
        return new ArrayList<>(stats);
    }
}
