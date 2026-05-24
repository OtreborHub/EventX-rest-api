package com.eventx.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponseDto(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        String paypalEmail,
        String phone,
        String artist,
        String location,
        List<String> activeTickets,
        List<String> lastTickets,
        List<String> reviews,
        LocalDateTime registrationDate
) {
}
