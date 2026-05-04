package org.project.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.user.UserRegistrationRequestDto;
import org.project.onlinebookstore.dto.user.UserResponseDto;
import org.project.onlinebookstore.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    @Operation(summary = "Register a user")
    @PostMapping("/register")
    public UserResponseDto register(@RequestBody UserRegistrationRequestDto requestDto) {
        return userService.register(requestDto);
    }

}
