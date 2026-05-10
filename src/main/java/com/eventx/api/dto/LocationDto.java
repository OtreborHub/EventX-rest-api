package com.eventx.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocationDto(
        @NotBlank(message = "userId obbligatorio") String userId,
        @NotBlank(message = "name obbligatorio") String name,
        String description,
        @NotBlank(message = "address obbligatorio") String address,
        @NotBlank(message = "city obbligatoria") String city,
        String province,
        String region,
        Integer cap,
        String gpsCoord
) {
}


