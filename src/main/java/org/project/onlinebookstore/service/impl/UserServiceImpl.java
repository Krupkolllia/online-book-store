package org.project.onlinebookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.user.UserRegistrationRequestDto;
import org.project.onlinebookstore.dto.user.UserResponseDto;
import org.project.onlinebookstore.exception.RegistrationException;
import org.project.onlinebookstore.mapper.UserMapper;
import org.project.onlinebookstore.model.User;
import org.project.onlinebookstore.repository.UserRepository;
import org.project.onlinebookstore.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User already registered with email: "
                    + requestDto.email());
        }

        User user = userMapper.toModel(requestDto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
