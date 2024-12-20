package ru.practicum.mapper.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.mapper.category.CategoryMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.service.category.CategoryService;
import ru.practicum.state.EventState;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UtilEventClass {

    private final CategoryMapper categoryMapper;

    private final CategoryService categoryService;

    public Event toEventFromNewEventDto(NewEventDto newEventDto, User user, CategoryDto category, Location location) {
        if (newEventDto == null || user == null || category == null || location == null) {
            return null;
        }


        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(categoryMapper.toEntity(category));
        event.setConfirmedRequests(0);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setInitiator(user);
        event.setLocation(location);
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setPublishedOn(null);
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setState(EventState.PENDING);
        event.setTitle(newEventDto.getTitle());

        return event;
    }

    public void updateEventFromDto(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest == null || event == null) {
            return;
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            CategoryDto categoryDto = categoryService.getCategoryById(updateEventAdminRequest.getCategory());
            event.setCategory(categoryMapper.toCategoryFromCategoryDto(categoryDto));
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(updateEventAdminRequest.getLocation());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
    }
}