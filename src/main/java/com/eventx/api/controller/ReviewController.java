package com.eventx.api.controller;

import com.eventx.api.dto.ReviewRequestDto;
import com.eventx.api.dto.ReviewResponseDto;
import com.eventx.api.service.ReviewService;
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
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> create(@Valid @RequestBody ReviewRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> findAll(@RequestParam(required = false) String userId,
                                                           @RequestParam(required = false) String locationId,
                                                           @RequestParam(required = false) String eventId,
                                                           @RequestParam(required = false) String artistId) {
        return ResponseEntity.ok(reviewService.findAll(userId, locationId, eventId, artistId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> update(@PathVariable String id,
                                                    @Valid @RequestBody ReviewRequestDto request) {
        return ResponseEntity.ok(reviewService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        reviewService.delete(id);
    }
}
