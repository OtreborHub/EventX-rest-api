package com.eventx.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ArtistRequestDto(
        @NotBlank(message = "userId obbligatorio") String userId,
        @NotBlank(message = "Nome obbligatorio") String firstName,
        @NotBlank(message = "Cognome obbligatorio") String lastName,
        @NotBlank(message = "Partita Iva obbligatoria") String piva,
        String alias,
        String phone
) {
}

