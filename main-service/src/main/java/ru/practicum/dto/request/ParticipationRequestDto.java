package ru.practicum.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.enums.RequestStatus;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParticipationRequestDto {
        Long id;
        EventFullDto event;
        UserDto requester;
        RequestStatus status;
        LocalDateTime createdOn;
}