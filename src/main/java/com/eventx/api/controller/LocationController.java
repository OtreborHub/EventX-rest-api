package com.eventx.api.controller;

import com.eventx.api.dto.LocationDto;
import com.eventx.api.dto.LocationResponseDto;
import com.eventx.api.service.LocationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationResponseDto> create(@Valid @RequestBody LocationDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<LocationResponseDto>> findAll(@RequestParam(required = false) String userId,
                                                             @RequestParam(required = false) String city,
                                                             @RequestParam(required = false) String name) {
        return ResponseEntity.ok(locationService.findAll(userId, city, name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(locationService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDto> update(@PathVariable String id,
                                                      @Valid @RequestBody LocationDto request) {
        return ResponseEntity.ok(locationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        locationService.delete(id);
    }
}
