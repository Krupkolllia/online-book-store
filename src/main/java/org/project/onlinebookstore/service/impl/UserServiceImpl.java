package org.project.onlinebookstore.service.impl;

import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.user.UserRegistrationRequestDto;
import org.project.onlinebookstore.dto.user.UserResponseDto;
import org.project.onlinebookstore.exception.EntityNotFoundException;
import org.project.onlinebookstore.exception.RegistrationException;
import org.project.onlinebookstore.mapper.UserMapper;
import org.project.onlinebookstore.model.Role;
import org.project.onlinebookstore.model.RoleName;
import org.project.onlinebookstore.model.User;
import org.project.onlinebookstore.repository.role.RoleRepository;
import org.project.onlinebookstore.repository.user.UserRepository;
import org.project.onlinebookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User already registered with email: "
                    + requestDto.email());
        }

        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));

        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException(RoleName.ROLE_USER + " not found"));
        user.setRoles(Set.of(role));

        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
