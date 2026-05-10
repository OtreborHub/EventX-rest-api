package com.eventx.api.service;

import com.eventx.api.dto.LocationDto;
import com.eventx.api.dto.LocationResponseDto;
import com.eventx.api.exceptions.ResourceNotFoundException;
import com.eventx.api.mapper.LocationMapper;
import com.eventx.api.models.Location;
import com.eventx.api.repository.LocationRepository;
import com.eventx.api.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public LocationResponseDto create(LocationDto request) {
        ensureUserExists(request.userId());

        Location location = Location.builder()
                .userId(request.userId())
                .name(request.name())
                .description(request.description())
                .address(request.address())
                .city(request.city())
                .province(request.province())
                .region(request.region())
                .cap(request.cap())
                .gpsCoord(request.gpsCoord())
                .reviews(new ArrayList<>())
                .registrationDate(LocalDateTime.now())
                .build();

        return LocationMapper.toResponse(locationRepository.save(location));
    }

    public List<LocationResponseDto> findAll(String userId, String city, String name) {
        return locationRepository.findAll().stream()
                .filter(location -> !StringUtils.hasText(userId) || userId.equals(location.getUserId()))
                .filter(location -> !StringUtils.hasText(city)
                        || location.getCity().toLowerCase().contains(city.toLowerCase()))
                .filter(location -> !StringUtils.hasText(name)
                        || location.getName().toLowerCase().contains(name.toLowerCase()))
                .map(LocationMapper::toResponse)
                .toList();
    }

    public LocationResponseDto findById(String id) {
        return LocationMapper.toResponse(getEntityById(id));
    }

    public LocationResponseDto update(String id, LocationDto request) {
        ensureUserExists(request.userId());
        Location location = getEntityById(id);
        location.setUserId(request.userId());
        location.setName(request.name());
        location.setDescription(request.description());
        location.setAddress(request.address());
        location.setCity(request.city());
        location.setProvince(request.province());
        location.setRegion(request.region());
        location.setCap(request.cap());
        location.setGpsCoord(request.gpsCoord());
        return LocationMapper.toResponse(locationRepository.save(location));
    }

    public void delete(String id) {
        Location location = getEntityById(id);
        locationRepository.delete(location);
    }

    public Location getEntityById(String id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location non trovata con id - " + id));
    }

    public void addReview(String locationId, String reviewId) {
        Location location = getEntityById(locationId);
        if (!location.getReviews().contains(reviewId)) {
            location.getReviews().add(reviewId);
            locationRepository.save(location);
        }
    }

    public void removeReview(String locationId, String reviewId) {
        Location location = getEntityById(locationId);
        location.getReviews().remove(reviewId);
        locationRepository.save(location);
    }

    private void ensureUserExists(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id - " + userId));
    }
}
