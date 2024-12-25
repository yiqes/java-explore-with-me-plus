package ru.practicum.mapper.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;

import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "eventDate", expression = "java(formatDateTime(event.getEventDate()))")
    public EventFullDto toEventFullDto(Event event);

    public Event toEvent(EventFullDto eventFullDto);

    public EventShortDto toEventShortDto(Event event);

    default String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
