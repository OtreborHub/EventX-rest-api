package com.eventx.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ArtistResponseDto(
        String id,
        String firstName,
        String lastName,
        String alias,
        String piva,
        String phone,
        List<String> activeEvents,
        List<String> lastEvents,
        List<String> reviews,
        LocalDateTime registrationDate
) {
}

