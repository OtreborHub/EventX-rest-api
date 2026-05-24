package com.eventx.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eventx.api.dto.UserResponseDto;
import com.eventx.api.exceptions.ConflictException;
import com.eventx.api.exceptions.GlobalExceptionHandler;
import com.eventx.api.security.CustomUserDetailsService;
import com.eventx.api.security.JwtAuthenticationFilter;
import com.eventx.api.security.JwtService;
import com.eventx.api.security.RestAccessDeniedHandler;
import com.eventx.api.security.RestAuthenticationEntryPoint;
import com.eventx.api.service.AuthService;
import com.eventx.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        value = UserController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class}
)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private AuthService authService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @MockBean
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @MockBean
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Test
    void registerShouldReturnCreatedUser() throws Exception {
        UserResponseDto responseDto = new UserResponseDto(
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
        );

        when(userService.register(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/utenti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "mario_rossi",
                                  "email": "mario.rossi@example.com",
                                  "password": "MySecret123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("user-1"))
                .andExpect(jsonPath("$.username").value("mario_rossi"));
    }

    @Test
    void registerShouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/utenti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "",
                                  "email": "not-an-email",
                                  "password": "123"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errore").value("Bad Request"));
    }

    @Test
    void registerShouldReturnConflictWhenUserAlreadyExists() throws Exception {
        when(userService.register(any())).thenThrow(new ConflictException("Email già in uso"));

        mockMvc.perform(post("/api/v1/utenti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "mario_rossi",
                                  "email": "mario.rossi@example.com",
                                  "password": "MySecret123"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.messaggio").value("Email già in uso"));
    }
}



