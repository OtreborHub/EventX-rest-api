package com.eventx.api.mapper;

import com.eventx.api.dto.ArtistResponseDto;
import com.eventx.api.models.Artist;
import java.util.List;

public final class ArtistMapper {

    private ArtistMapper() {
    }

    public static ArtistResponseDto toResponse(Artist artist) {
        return new ArtistResponseDto(
                artist.getId(),
                artist.getFirstName(),
                artist.getLastName(),
                artist.getAlias(),
                artist.getPiva(),
                artist.getPhone(),
                List.copyOf(artist.getActiveEvents()),
                List.copyOf(artist.getLastEvents()),
                List.copyOf(artist.getReviews()),
                artist.getRegistrationDate()
        );
    }
}

