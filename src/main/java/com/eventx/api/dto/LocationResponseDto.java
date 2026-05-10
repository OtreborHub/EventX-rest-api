package com.eventx.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record LocationResponseDto(
        String id,
        String userId,
        String name,
        String description,
        String address,
        String city,
        String province,
        String region,
        Integer cap,
        String gpsCoord,
        List<String> reviews,
        LocalDateTime registrationDate
) {
}
