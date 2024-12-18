package ru.practicum.mapper.user;

import org.mapstruct.Mapper;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.User;

@Mapper(componentModel = "spring")
public interface UserShortMapper {

    UserShortDto toUserShortDto(User user);
}
