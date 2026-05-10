package com.eventx.api.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eventx.api.controller.EventController;
import com.eventx.api.controller.UserController;
import com.eventx.api.dto.LoginResponseDto;
import com.eventx.api.dto.UserResponseDto;
import com.eventx.api.exceptions.GlobalExceptionHandler;
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
    void protectedEndpointShouldReturnUnauthorizedWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/eventi"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.errore").value("Non autorizzato"));
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
                        "Mario",
                        "Rossi",
                        "",
                        "mario.rossi@paypal.com",
                        false,
                        false,
                        false,
                        List.of(),
                        List.of(),
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

        mockMvc.perform(get("/api/v1/eventi")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.messaggio").value("Token JWT non valido o scaduto"));
    }
}

