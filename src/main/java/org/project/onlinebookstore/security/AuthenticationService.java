package org.project.onlinebookstore.security;

import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.user.UserLoginRequestDto;
import org.project.onlinebookstore.dto.user.UserLoginResponseDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto login(UserLoginRequestDto request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        String username = authentication.getName();
        String token = jwtUtil.generateToken(username);

        return new UserLoginResponseDto(token);
    }
}
