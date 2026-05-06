package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.user.UserRegistrationRequestDto;
import org.project.onlinebookstore.dto.user.UserResponseDto;
import org.project.onlinebookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
