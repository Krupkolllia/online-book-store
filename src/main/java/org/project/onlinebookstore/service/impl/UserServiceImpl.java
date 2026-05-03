package org.project.onlinebookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.user.UserRegistrationRequestDto;
import org.project.onlinebookstore.dto.user.UserResponseDto;
import org.project.onlinebookstore.exception.RegistrationException;
import org.project.onlinebookstore.mapper.UserMapper;
import org.project.onlinebookstore.model.Role;
import org.project.onlinebookstore.model.RoleName;
import org.project.onlinebookstore.model.User;
import org.project.onlinebookstore.repository.RoleRepository;
import org.project.onlinebookstore.repository.UserRepository;
import org.project.onlinebookstore.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User already registered with email: "
                    + requestDto.email());
        }

        User user = userMapper.toModel(requestDto);
        assignDefaultRole(user);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    private void assignDefaultRole(User user) {
        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        user.setRoles(new HashSet<>(Set.of(role)));
    }
}
