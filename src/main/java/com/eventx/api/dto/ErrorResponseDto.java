package com.eventx.api.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        int status,
        String errore,
        String messaggio,
        LocalDateTime timestamp
) {
}
