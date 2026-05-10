package com.eventx.api.mapper;

import com.eventx.api.dto.ReviewResponseDto;
import com.eventx.api.models.Review;

public final class ReviewMapper {

    private ReviewMapper() {
    }

    public static ReviewResponseDto toResponse(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getUserId(),
                review.getLocationId(),
                review.getEventId(),
                review.getArtistId(),
                review.getVote(),
                review.getTitle(),
                review.getDescription(),
                review.getRegistrationDate()
        );
    }
}
