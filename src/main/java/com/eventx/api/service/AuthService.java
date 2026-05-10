package com.eventx.api.service;

import com.eventx.api.dto.LoginRequestDto;
import com.eventx.api.dto.LoginResponseDto;
import com.eventx.api.dto.UsernameLoginRequestDto;
import com.eventx.api.exceptions.UnauthorizedException;
import com.eventx.api.mapper.UserMapper;
import com.eventx.api.models.User;
import com.eventx.api.repository.UserRepository;
import com.eventx.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDto loginByEmail(LoginRequestDto request) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new UnauthorizedException("Credenziali non valide"));
        return buildLoginResponse(user, request.password());
    }

    public LoginResponseDto loginByUsername(UsernameLoginRequestDto request) {
        User user = userRepository.findByUsernameIgnoreCase(request.username())
                .orElseThrow(() -> new UnauthorizedException("Credenziali non valide"));
        return buildLoginResponse(user, request.password());
    }

    private LoginResponseDto buildLoginResponse(User user, String rawPassword) {
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new UnauthorizedException("Credenziali non valide");
        }
        return new LoginResponseDto(
                jwtService.generateToken(user),
                "Bearer",
                UserMapper.toResponse(user)
        );
    }
}
