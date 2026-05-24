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
                user.getPaypalEmail(),
                user.getPhone(),
                user.getArtist(),
                user.getLocation(),
                List.copyOf(user.getActiveTickets()),
                List.copyOf(user.getLastTickets()),
                List.copyOf(user.getReviews()),
                user.getRegistrationDate()
        );
    }

public static User toEntity(UserResponseDto dto) {
        return User.builder()
                .id(dto.id())
                .username(dto.username())
                .email(dto.email())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .paypalEmail(dto.paypalEmail())
                .phone(dto.phone())
                .artist(dto.artist())
                .location(dto.location())
                .activeTickets(List.copyOf(dto.activeTickets()))
                .lastTickets(List.copyOf(dto.lastTickets()))
                .reviews(List.copyOf(dto.reviews()))
                .registrationDate(dto.registrationDate())
                .build();
    }
}
