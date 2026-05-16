package com.eventx.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponseDto(
        String id,
        String username,
        String email,
        String paypalEmail,
        boolean isArtist,
        boolean isLocation,
        List<String> activeTickets,
        List<String> lastTickets,
        List<String> reviews,
        LocalDateTime registrationDate
) {
}
