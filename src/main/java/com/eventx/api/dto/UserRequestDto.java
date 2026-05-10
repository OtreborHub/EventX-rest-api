package com.eventx.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
        @NotBlank(message = "username obbligatorio") String username,
        @NotBlank(message = "email obbligatoria") @Email(message = "email non è un indirizzo valido") String email,
        @NotBlank(message = "password obbligatoria") @Size(min = 8, message = "password deve avere almeno 8 caratteri") String password,
        String firstName,
        String lastName,
        String phone,
        @Email(message = "paypalEmail non è un indirizzo valido") String paypalEmail
) {
}


