package ru.practicum.service.event;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.SearchEventsParamAdmin;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.enums.RequestStatus;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.mapper.event.UtilEventClass;
import ru.practicum.mapper.request.RequestMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LocationRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.SearchEventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.category.CategoryService;
import ru.practicum.state.AdminStateAction;
import ru.practicum.state.EventState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventServiceImpl implements EventService {

    EventRepository eventRepository;
    UserRepository userRepository;
    RequestRepository requestRepository;
    RequestMapper requestMapper;
    EventMapper eventMapper;
    CategoryService categoryService;
    UtilEventClass utilEventClass;
    LocationRepository locationRepository;
    SearchEventRepository searchEventRepository;
    CategoryRepository categoryRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            RequestRepository requestRepository, RequestMapper requestMapper,
                            EventMapper eventMapper, CategoryService categoryService, UtilEventClass utilEventClass,
                            LocationRepository locationRepository, SearchEventRepository searchEventRepository,
                            CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
        this.eventMapper = eventMapper;
        this.categoryService = categoryService;
        this.utilEventClass = utilEventClass;
        this.locationRepository = locationRepository;
        this.searchEventRepository = searchEventRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ParticipationRequestDto> getRequestByUserId(Long userId) {
        userRepository.findById(userId);
        List<Request> requestList = requestRepository.findAllByRequesterId(userId);
        return requestList.stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getRequestByUserAndEvent(Long userId, Long eventId) {
        List<Request> requestList = requestRepository.findAllByEventId(eventId);
        return requestList.stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " not found!", ""));
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " not found!", ""));
        requestToEventVerification(user, event);
        Request request = requestMapper.formUserAndEventToRequest(user, event);
        requestRepository.save(request);
        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id=" + userId + " not found!", ""));
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Object with id=" + requestId + " was not found!", "")
        );
        request.setStatus(RequestStatus.CANCELED);
        request = requestRepository.save(request);

        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult requestUpdateStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        List<Request> requestList = requestRepository
                .findByIdInAndEventId(eventRequestStatusUpdateRequest.getRequestIds(), eventId);
        return requestUpdateVerification(eventId, requestList, eventRequestStatusUpdateRequest.getStatus());
    }


    private void requestToEventVerification(User user, Event event) {
        long userId = user.getId();
        if (userId == event.getInitiator().getId() && event.getInitiator() != null) {
            throw new IllegalArgumentException("Initiator of event with id=" + userId +
                    " cannot add request for participation in his own event");
        }
        if (event.getPublishedOn() == null) {
            throw new ConflictException("", "You cannot participate in an unpublished event id=" + event.getId());
        }
        if (event.getParticipantLimit() != 0) {
            long countRequests = requestRepository.countByStatusAndEventId(RequestStatus.CONFIRMED, event.getId());
            if (countRequests >= event.getParticipantLimit()) {
                throw new ConflictException("The event with id=" + event.getId() + " has reached the limit of participation requests", "");
            }
        }
    }

    private EventRequestStatusUpdateResult requestUpdateVerification(Long eventId, List<Request> requestList, RequestStatus status) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        for (Request request : requestList) {
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("You can only change the status of pending applications", "");
            }
            long count = requestRepository.countByStatusAndEventId(RequestStatus.CONFIRMED, eventId);

            Event event = request.getEvent();
            if (count >= event.getParticipantLimit()) {
                throw new ConflictException("The event with id=" + event.getId() +
                        " has reached the limit of participation requests", "");
            }
            if (request.getEvent().getId().equals(eventId)) {
                request.setStatus(status);
                requestRepository.save(request);
                confirmedRequests.add(requestMapper.toParticipationRequestDto(request));
            } else {
                rejectedRequests.add(requestMapper.toParticipationRequestDto(request));
            }
        }
        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsForUser(Long userId, Integer from, Integer size) {
        List<Event> events = eventRepository.findByInitiatorId(userId, PageRequest.of(from, size));

        return events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto eventDto) {
        User initializer = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " not found!", ""));
        CategoryDto category = categoryService.getCategoryById(eventDto.getCategory());
        Location location = eventDto.getLocation();
        locationRepository.save(location);
        Event event = utilEventClass.toEventFromNewEventDto(eventDto, initializer, category, location);

        if (eventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        if (eventDto.getPaid() == null) {
            event.setPaid(false);
        }
        if (eventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }

        event = eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByIdForUser(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " not found!", ""));
        return eventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto changeEvent(Long userId, Long eventId, UpdateEventAdminRequest eventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " not found!", ""));
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("event with id=" + eventId + "published and cannot be changed", "");
        }
        if (eventDto.getStateAction() != null) {
            switch (eventDto.getStateAction()) {
                case SEND_TO_REVIEW -> event.setState(EventState.PENDING);
                case CANCEL_REVIEW -> event.setState(EventState.CANCELED);
            }
        }
        utilEventClass.updateEventFromDto(event, eventDto);

        event = eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> getEventsForAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                Integer from, Integer size) {
        checkDateTime(rangeStart, rangeEnd);
        SearchEventsParamAdmin searchEventsParamAdmin = SearchEventsParamAdmin.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();
        List<Event> events = searchEventRepository.getEventsByParamForAdmin(searchEventsParamAdmin);
        return events.stream().map(e -> eventMapper.toEventFullDto(e)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequest request, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " not found!", ""));
        Category category;
        if (request.getCategory() != null) {
            category = categoryRepository.findById(request.getCategory()).orElseThrow(() ->
                    new NotFoundException("Category with id=" + request.getCategory() + " not found!", ""));
        } else {
            category = event.getCategory();
        }
        Location location = checkAndSaveLocation(request.getLocation());
        checkTimeBeforeStart(request.getEventDate(), 1);
        checkTimeBeforeStart(event.getEventDate(), 1);

        if (AdminStateAction.PUBLISH_EVENT.equals(request.getStateAction())) {
            if (event.getState().equals(EventState.PENDING)) {
                event = utilEventClass.updateEvent(event, request, category, location);
                event.setPublishedOn(LocalDateTime.now());
                event.setState(EventState.PUBLISHED);
            } else {
                throw new ConflictException("Event is not PENDING!", "");
            }
        } else if (AdminStateAction.REJECT_EVENT.equals(request.getStateAction())) {
            if (!event.getState().equals(EventState.PUBLISHED)) {
                event = utilEventClass.updateEvent(event, request, category, location);
                event.setState(EventState.CANCELED);
            } else {
                throw new ConflictException("PUBLISHED events can't be cancelled!", "event should be PENDING or CANCELED");

            }
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    private void checkDateTime(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("start time can't be after end time", "time range is incorrect");
        }
    }

    private Location checkAndSaveLocation(Location newLocation) {
        if (newLocation == null) {
            return null;
        }
        Location location = locationRepository.findByLatAndLon(newLocation.getLat(), newLocation.getLon())
                .orElse(null);
        if (location == null) {
            return locationRepository.save(newLocation);
        }
        return location;
    }

    private void checkTimeBeforeStart(LocalDateTime checkingTime, Integer plusHour) {
        if (checkingTime != null && checkingTime.isBefore(LocalDateTime.now().plusHours(plusHour))) {
            throw new ValidationException("updated time should be " + plusHour + "ahead then current time!", "not enough time before event");
        }
    }


}
