package com.eventx.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponseDto(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        String phone,
        String paypalEmail,
        boolean isAdmin,
        boolean isArtist,
        boolean isLocation,
        List<String> activeTickets,
        List<String> lastTickets,
        List<String> activeEvents,
        List<String> lastEvents,
        List<String> reviews,
        LocalDateTime registrationDate
) {
}
