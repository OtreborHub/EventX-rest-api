package com.eventx.api.dto;

import jakarta.validation.constraints.NotBlank;

public record TicketBuyRequestDto(
        @NotBlank String userId,
        @NotBlank String eventId,
        String position
) {
}

