package com.eventx.api.dto;

public record LoginResponseDto(
        String token,
        String tokenType,
        UserResponseDto user
) {
}
