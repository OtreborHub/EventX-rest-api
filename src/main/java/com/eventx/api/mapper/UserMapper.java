package com.eventx.api.mapper;

import com.eventx.api.dto.UserResponseDto;
import com.eventx.api.models.User;
import java.util.List;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponseDto toResponse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getPaypalEmail(),
                user.isAdmin(),
                user.isArtist(),
                user.isLocation(),
                List.copyOf(user.getActiveTickets()),
                List.copyOf(user.getLastTickets()),
                List.copyOf(user.getActiveEvents()),
                List.copyOf(user.getLastEvents()),
                List.copyOf(user.getReviews()),
                user.getRegistrationDate()
        );
    }
}
