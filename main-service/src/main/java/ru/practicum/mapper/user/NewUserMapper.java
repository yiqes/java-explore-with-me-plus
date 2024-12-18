package ru.practicum.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.model.user.User;

@Mapper(componentModel = "spring")
public interface NewUserMapper {
    @Mapping(target = "id", ignore = true)
    User fromNewUserRequest(NewUserRequest newUserRequest);
}
