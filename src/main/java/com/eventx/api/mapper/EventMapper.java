package com.eventx.api.mapper;

import com.eventx.api.dto.EventResponseDto;
import com.eventx.api.models.Event;
import java.util.List;

public final class EventMapper {

    private EventMapper() {
    }

    public static EventResponseDto toResponse(Event event) {
        return new EventResponseDto(
                event.getId(),
                event.getCreationId(),
                event.getName(),
                event.getDescription(),
                event.getLocationId(),
                event.getDate(),
                event.getDuration(),
                event.getPrice(),
                event.getCapacity(),
                event.getExpectedPublic(),
                event.getStatus(),
                List.copyOf(event.getReviews()),
                event.getRegistrationDate()
        );
    }
}
