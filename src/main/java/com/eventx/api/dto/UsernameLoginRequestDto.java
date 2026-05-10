package com.eventx.api.dto;

import jakarta.validation.constraints.NotBlank;

public record UsernameLoginRequestDto(
        @NotBlank(message = "username obbligatorio") String username,
        @NotBlank(message = "password obbligatoria") String password
) {
}


