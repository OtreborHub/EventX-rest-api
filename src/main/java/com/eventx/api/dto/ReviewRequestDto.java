package com.eventx.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewRequestDto(
        @NotBlank String userId,
        String locationId,
        String eventId,
        String artistId,
        @NotNull @Min(0) @Max(5) Integer vote,
        String title,
        String description
) {
}

