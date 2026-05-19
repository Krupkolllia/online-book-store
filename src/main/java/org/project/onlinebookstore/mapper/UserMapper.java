package org.project.onlinebookstore.mapper;

import org.mapstruct.Mapper;
import org.project.onlinebookstore.config.MapStructConfig;
import org.project.onlinebookstore.dto.user.UserRegistrationRequestDto;
import org.project.onlinebookstore.dto.user.UserResponseDto;
import org.project.onlinebookstore.model.user.User;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto registrationRequestDto);
}
