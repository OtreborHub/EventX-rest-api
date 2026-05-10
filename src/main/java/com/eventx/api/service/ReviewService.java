package com.eventx.api.service;

import com.eventx.api.dto.ReviewRequestDto;
import com.eventx.api.dto.ReviewResponseDto;
import com.eventx.api.exceptions.BadRequestException;
import com.eventx.api.exceptions.ResourceNotFoundException;
import com.eventx.api.mapper.ReviewMapper;
import com.eventx.api.models.Review;
import com.eventx.api.repository.ReviewRepository;
import com.eventx.api.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EventService eventService;
    private final LocationService locationService;

    public ReviewResponseDto create(ReviewRequestDto request) {
        validateTargets(request.locationId(), request.eventId(), request.artistId());
        userService.getEntityById(request.userId());
        validateReferencedResources(request.locationId(), request.eventId(), request.artistId());

        Review review = Review.builder()
                .userId(request.userId())
                .locationId(normalize(request.locationId()))
                .eventId(normalize(request.eventId()))
                .artistId(normalize(request.artistId()))
                .vote(request.vote())
                .title(request.title())
                .description(request.description())
                .registrationDate(LocalDateTime.now())
                .build();

        Review saved = reviewRepository.save(review);
        linkReview(saved);
        return ReviewMapper.toResponse(saved);
    }

    public List<ReviewResponseDto> findAll(String userId, String locationId, String eventId, String artistId) {
        return reviewRepository.findAll().stream()
                .filter(review -> !StringUtils.hasText(userId) || userId.equals(review.getUserId()))
                .filter(review -> !StringUtils.hasText(locationId) || locationId.equals(review.getLocationId()))
                .filter(review -> !StringUtils.hasText(eventId) || eventId.equals(review.getEventId()))
                .filter(review -> !StringUtils.hasText(artistId) || artistId.equals(review.getArtistId()))
                .map(ReviewMapper::toResponse)
                .toList();
    }

    public ReviewResponseDto findById(String id) {
        return ReviewMapper.toResponse(getEntityById(id));
    }

    public ReviewResponseDto update(String id, ReviewRequestDto request) {
        Review review = getEntityById(id);
        validateTargets(request.locationId(), request.eventId(), request.artistId());
        userService.getEntityById(request.userId());
        validateReferencedResources(request.locationId(), request.eventId(), request.artistId());

        unlinkReview(review);

        review.setUserId(request.userId());
        review.setLocationId(normalize(request.locationId()));
        review.setEventId(normalize(request.eventId()));
        review.setArtistId(normalize(request.artistId()));
        review.setVote(request.vote());
        review.setTitle(request.title());
        review.setDescription(request.description());

        Review saved = reviewRepository.save(review);
        linkReview(saved);
        return ReviewMapper.toResponse(saved);
    }

    public void delete(String id) {
        Review review = getEntityById(id);
        unlinkReview(review);
        reviewRepository.delete(review);
    }

    public Review getEntityById(String id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recensione non trovata con id - " + id));
    }

    private void validateTargets(String locationId, String eventId, String artistId) {
        if (!StringUtils.hasText(locationId) && !StringUtils.hasText(eventId) && !StringUtils.hasText(artistId)) {
            throw new BadRequestException("Almeno uno tra locationId, eventId e artistId deve essere valorizzato");
        }
    }

    private void validateReferencedResources(String locationId, String eventId, String artistId) {
        if (StringUtils.hasText(locationId)) {
            locationService.getEntityById(locationId);
        }
        if (StringUtils.hasText(eventId)) {
            eventService.getEntityById(eventId);
        }
        if (StringUtils.hasText(artistId)) {
            userRepository.findById(artistId)
                    .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id - " + artistId));
        }
    }

    private void linkReview(Review review) {
        userService.addReview(review.getUserId(), review.getId());
        if (StringUtils.hasText(review.getEventId())) {
            eventService.addReview(review.getEventId(), review.getId());
        }
        if (StringUtils.hasText(review.getLocationId())) {
            locationService.addReview(review.getLocationId(), review.getId());
        }
    }

    private void unlinkReview(Review review) {
        userService.removeReview(review.getUserId(), review.getId());
        if (StringUtils.hasText(review.getEventId())) {
            eventService.removeReview(review.getEventId(), review.getId());
        }
        if (StringUtils.hasText(review.getLocationId())) {
            locationService.removeReview(review.getLocationId(), review.getId());
        }
    }

    private String normalize(String value) {
        return StringUtils.hasText(value) ? value : null;
    }
}
