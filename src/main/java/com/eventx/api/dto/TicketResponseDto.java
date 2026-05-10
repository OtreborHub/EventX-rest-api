package com.eventx.api.dto;

import java.time.LocalDateTime;

public record TicketResponseDto(
        String id,
        String position,
        String userId,
        String eventId,
        boolean isBlockchain,
        boolean isWinner,
        String hash,
        LocalDateTime registrationDate
) {
}
