package com.eventx.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequestDto(
        @NotBlank String currentPassword,
        @Email String email,
        String firstName,
        String lastName,
        String phone,
        @Email String paypalEmail
) {
}

