package com.eventx.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eventx.api.dto.ChangePasswordRequestDto;
import com.eventx.api.dto.UserRequestDto;
import com.eventx.api.exceptions.ConflictException;
import com.eventx.api.exceptions.UnauthorizedException;
import com.eventx.api.models.User;
import com.eventx.api.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerShouldCreateUser() {
        UserRequestDto request = new UserRequestDto(
                "mario_rossi",
                "mario.rossi@example.com",
                "MySecret123",
                "mario.rossi@paypal.com"
        );

        when(userRepository.findByEmailIgnoreCase(request.email())).thenReturn(Optional.empty());
        when(userRepository.findByUsernameIgnoreCase(request.username())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId("user-1");
            user.setRegistrationDate(LocalDateTime.now());
            return user;
        });

        var response = userService.register(request);

        assertThat(response.id()).isEqualTo("user-1");
        assertThat(response.username()).isEqualTo("mario_rossi");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPasswordHash()).isEqualTo("encoded-password");
    }

    @Test
    void registerShouldThrowConflictWhenEmailAlreadyExists() {
        UserRequestDto request = new UserRequestDto(
                "mario_rossi",
                "mario.rossi@example.com",
                "MySecret123",
                null
        );

        when(userRepository.findByEmailIgnoreCase(request.email())).thenReturn(Optional.of(User.builder().id("existing").build()));

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Email già in uso");
    }

    @Test
    void changePasswordShouldRejectWrongCurrentPassword() {
        User user = User.builder()
                .id("user-1")
                .passwordHash("encoded-current")
                .build();

        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "encoded-current")).thenReturn(false);

        assertThatThrownBy(() -> userService.changePassword("user-1", new ChangePasswordRequestDto("wrong-password", "NewSecret456")))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Credenziali non valide");
    }
}
