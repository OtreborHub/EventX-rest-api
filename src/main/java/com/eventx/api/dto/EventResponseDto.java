package com.eventx.api.dto;

import com.eventx.api.models.EventStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record EventResponseDto(
        String id,
        String creationId,
        String name,
        String description,
        String locationId,
        LocalDateTime date,
        Integer duration,
        BigDecimal price,
        Integer capacity,
        Integer expectedPublic,
        EventStatus status,
        List<String> reviews,
        LocalDateTime registrationDate
) {
}
