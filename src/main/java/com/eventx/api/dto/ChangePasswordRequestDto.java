package com.eventx.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDto(
        @NotBlank String currentPassword,
        @NotBlank @Size(min = 8, message = "password deve avere almeno 8 caratteri") String newPassword
) {
}

