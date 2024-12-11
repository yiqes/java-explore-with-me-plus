package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.*;
import ru.practicum.model.EndpointHit;

@Mapper(componentModel = "spring")
public abstract class EndpointHitMapper {

    @Mapping(target = "id", ignore = true)
    public abstract EndpointHitDto toEndpointHitDto(EndpointHitSaveRequestDto endpointHitSaveRequestDto);

    public EndpointHit toEndpointHit(EndpointHitSaveRequestDto endpointHitSaveRequestDto) {
        if (endpointHitSaveRequestDto == null) {
            return null;
        }
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(endpointHitSaveRequestDto.getApp());
        endpointHit.setUri(endpointHitSaveRequestDto.getUri());
        endpointHit.setIp(endpointHitSaveRequestDto.getIp());
        endpointHit.setTimestamp(endpointHitSaveRequestDto.getTimestamp());
        return endpointHit;
    }

    public abstract EndpointHit toEndpointHit(EndpointHitDto endpointHitDto);

    public EndpointHitResponseDto toResponseDto(EndpointHit endpointHit) {
        if (endpointHit == null) {
            return null;
        }
        EndpointHitResponseDto endpointHitResponseDto = new EndpointHitResponseDto();
        endpointHitResponseDto.setApp(endpointHit.getApp());
        endpointHitResponseDto.setUri(endpointHit.getUri());
        endpointHitResponseDto.setIp(endpointHit.getIp());
        return endpointHitResponseDto;
    }

    public abstract ViewStatsDto toViewStatsDto(SecondaryViewStatsDto secondaryViewStatsDto);
}
