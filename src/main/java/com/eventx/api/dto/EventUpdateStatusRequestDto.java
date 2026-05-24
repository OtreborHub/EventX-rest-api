package com.eventx.api.dto;

import com.eventx.api.models.EventStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventUpdateStatusRequestDto(
        @NotBlank String plannerId,
        @NotBlank String name,
        String description,
        @NotBlank String locationId,
        @NotNull LocalDateTime date,
        @NotNull @Min(1) Integer duration,
        @Min(0) BigDecimal price,
        @NotNull @Min(1) Integer capacity,
        EventStatus status
) {
}