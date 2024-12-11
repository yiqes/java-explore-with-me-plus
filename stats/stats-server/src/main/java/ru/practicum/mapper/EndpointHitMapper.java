package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.*;
import ru.practicum.model.EndpointHit;

@Mapper(componentModel = "spring")
public abstract class EndpointHitMapper {

    public abstract EndpointHit toEndpointHit(EndpointHitSaveRequestDto endpointHitSaveRequestDto);

    public abstract EndpointHitResponseDto toResponseDto(EndpointHit endpointHit);

}
