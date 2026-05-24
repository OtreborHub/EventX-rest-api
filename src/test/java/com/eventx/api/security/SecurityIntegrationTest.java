package com.eventx.api.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eventx.api.controller.EventController;
import com.eventx.api.controller.UserController;
import com.eventx.api.dto.LoginResponseDto;
import com.eventx.api.dto.UserResponseDto;
import com.eventx.api.exceptions.GlobalExceptionHandler;
import com.eventx.api.mapper.UserMapper;
import com.eventx.api.models.User;
import com.eventx.api.service.AuthService;
import com.eventx.api.service.EventService;
import com.eventx.api.service.UserService;
import io.jsonwebtoken.JwtException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({UserController.class, EventController.class})
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class, GlobalExceptionHandler.class})
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private AuthService authService;
    @MockBean
    private EventService eventService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void eventsListShouldbefreetoReturn() throws Exception {
        when(eventService.findAll(any(), any(), any(), any(), any(), any())).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/eventi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void usersListShouldBeProtectedWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/utenti"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void publicLoginEndpointShouldBeAccessibleWithoutToken() throws Exception {
        when(authService.loginByEmail(any())).thenReturn(new LoginResponseDto(
                "jwt-token",
                "Bearer",
                new UserResponseDto(
                        "user-1",
                        "mario_rossi",
                        "mario.rossi@example.com",
                        "mario",
                        "rossi",
                        "mario.rossi@paypal.com",
                        "",
                        "",
                        "",
                        List.of(),
                        List.of(),
                        List.of(),
                        LocalDateTime.parse("2026-05-10T10:00:00")
                )
        ));

        mockMvc.perform(post("/api/v1/utenti/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "mario.rossi@example.com",
                                  "password": "MySecret123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void invalidJwtShouldReturnUnauthorized() throws Exception {
        when(jwtService.extractUserId("invalid-token")).thenThrow(new JwtException("invalid token"));

        mockMvc.perform(get("/api/v1/utenti")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.messaggio").value("Token JWT non valido o scaduto"));
    }

    @Test
    void updateProfileWithValidTokenShouldSucceed() throws Exception {
        // 1️⃣ REGISTRAZIONE
        String userId = "user-123";
        String email = "mario.rossi@example.com";
        String password = "MySecret123";
        String validToken = "valid-jwt-token";

        UserResponseDto registeredUser = new UserResponseDto(
                userId,
                "mario_rossi",
                email,
                "mario",
                "rossi",
                "mario.rossi@paypal.com",
                "",
                "",
                "",
                List.of(),
                List.of(),
                List.of(),
                LocalDateTime.parse("2026-05-10T10:00:00")
        );

        when(userService.register(any())).thenReturn(registeredUser);
        User user = UserMapper.toEntity(registeredUser);

        // 2️⃣ LOGIN - otteniamo il token
        UserPrincipal userPrincipal = new UserPrincipal(user, "");

        when(authService.loginByEmail(any())).thenReturn(new LoginResponseDto(
                validToken,
                "Bearer",
                registeredUser
        ));
        when(jwtService.extractUserId(validToken)).thenReturn(userId);
        when(jwtService.isTokenValid(validToken, userPrincipal)).thenReturn(true);
        when(customUserDetailsService.loadUserByUsername(userId)).thenReturn(userPrincipal);

        // 3️⃣ AGGIORNA PROFILO con token valido
        UserResponseDto updatedUser = new UserResponseDto(
                userId,
                "mario_rossi",
                "mario.nuovo@example.com",   // Email modificata
                "Mario",// Nome
                "Rossi", // Cognome
                "mario.nuovo@paypal.com", // PayPal modificato
                "",
                "",
                "",
                List.of(),
                List.of(),
                List.of(),
                LocalDateTime.parse("2026-05-10T10:00:00")
        );

        when(userService.updateProfile(any())).thenReturn(updatedUser);

        mockMvc.perform(post("/api/v1/utenti/profilo")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "userId": "user-123",
                              "password": "MySecret123",
                              "email": "mario.nuovo@example.com",
                              "firstName": "Mario",
                              "lastName": "Rossi",
                              "phone": "3331234567",
                              "paypalEmail": "mario.nuovo@paypal.com"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value("mario.nuovo@example.com"))
                .andExpect(jsonPath("$.paypalEmail").value("mario.nuovo@paypal.com"));
    }

    @Test
    void updateProfileWithoutTokenShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/utenti/profilo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "userId": "user-123",
                              "currentPassword": "MySecret123",
                              "email": "mario.nuovo@example.com",
                              "firstName": "Mario",
                              "lastName": "Rossi",
                              "phoneNumber": "3331234567",
                              "paypalEmail": "mario.nuovo@paypal.com"
                            }
                            """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.messaggio").value("Token JWT non valido o scaduto"));
    }

    @Test
    void updateProfileWithInvalidTokenShouldReturnUnauthorized() throws Exception {
        when(jwtService.extractUserId("invalid-token")).thenThrow(new JwtException("invalid token"));

        mockMvc.perform(post("/api/v1/utenti/profilo")
                        .header("Authorization", "Bearer invalid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "userId": "user-123",
                              "currentPassword": "MySecret123",
                              "email": "mario.nuovo@example.com",
                              "firstName": "Mario",
                              "lastName": "Rossi",
                              "phoneNumber": "3331234567",
                              "paypalEmail": "mario.nuovo@paypal.com"
                            }
                            """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.messaggio").value("Token JWT non valido o scaduto"));
    }
}

