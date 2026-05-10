package com.eventx.api.mapper;

import com.eventx.api.dto.LocationResponseDto;
import com.eventx.api.models.Location;
import java.util.List;

public final class LocationMapper {

    private LocationMapper() {
    }

    public static LocationResponseDto toResponse(Location location) {
        return new LocationResponseDto(
                location.getId(),
                location.getUserId(),
                location.getName(),
                location.getDescription(),
                location.getAddress(),
                location.getCity(),
                location.getProvince(),
                location.getRegion(),
                location.getCap(),
                location.getGpsCoord(),
                List.copyOf(location.getReviews()),
                location.getRegistrationDate()
        );
    }
}
