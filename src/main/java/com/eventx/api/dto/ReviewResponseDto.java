package com.eventx.api.dto;

import java.time.LocalDateTime;

public record ReviewResponseDto(
        String id,
        String userId,
        String locationId,
        String eventId,
        String artistId,
        Integer vote,
        String title,
        String description,
        LocalDateTime registrationDate
) {
}
