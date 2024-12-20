package ru.practicum.mapper.request;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.enums.RequestStatus;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestMapper {

    UserMapper userMapper;
    EventMapper eventMapper;
    UserRepository userRepository;
    EventRepository eventRepository;

    @Autowired

    public RequestMapper(UserMapper userMapper, EventMapper eventMapper, UserRepository userRepository, EventRepository eventRepository) {
        this.userMapper = userMapper;
        this.eventMapper = eventMapper;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                eventMapper.toEventFullDto(request.getEvent()),
                userMapper.toUserDto(request.getRequester()),
                request.getStatus(),
                request.getCreatedOn()
        );
    }

    public Request toRequest(ParticipationRequestDto participationRequestDto, Long requesterId, Long eventId) {
        return new Request(
                null,
                eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found", "")),
                userRepository.findById(requesterId).orElseThrow(
                        () -> new NotFoundException("User with id=" + requesterId + " not found!", "")),
                participationRequestDto.getStatus(),
                participationRequestDto.getCreatedOn()
        );
    }

    public Request formUserAndEventToRequest(User user, Event event) {
        if (user == null || event == null) {
            return null;
        }

        Request request = new Request();
        request.setEvent(event);
        request.setRequester(user);
        request.setStatus(setStatus(event));
        request.setCreatedOn(LocalDateTime.now());

        return request;
    }

    private RequestStatus setStatus(Event event) {
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return RequestStatus.CONFIRMED;
        }
        return RequestStatus.PENDING;
    }
}
