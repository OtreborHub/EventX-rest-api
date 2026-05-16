package com.eventx.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ArtistRequestDto(
        @NotBlank(message = "userId obbligatorio") String userId,
        @NotBlank(message = "firstName obbligatorio") String firstName,
        @NotBlank(message = "lastName obbligatorio") String lastName,
        String alias,
        String piva,
        String phone
) {
}

