package com.eventx.api.service;

import com.eventx.api.dto.ArtistRequestDto;
import com.eventx.api.dto.ArtistResponseDto;
import com.eventx.api.exceptions.ConflictException;
import com.eventx.api.exceptions.ResourceNotFoundException;
import com.eventx.api.mapper.ArtistMapper;
import com.eventx.api.models.Artist;
import com.eventx.api.models.EventStatus;
import com.eventx.api.repository.ArtistRepository;
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
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ArtistResponseDto create(ArtistRequestDto request) {
        ensureUserExists(request.userId());
        if (artistRepository.existsById(request.userId()) || locationRepository.existsById(request.userId())) {
            throw new ConflictException("L'utente è già registrato come artista o luogo");
        }

        Artist artist = Artist.builder()
                .id(request.userId())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .alias(request.alias())
                .piva(request.piva())
                .phone(request.phone())
                .activeEvents(new ArrayList<>())
                .lastEvents(new ArrayList<>())
                .reviews(new ArrayList<>())
                .registrationDate(LocalDateTime.now())
                .build();

        Artist saved = artistRepository.save(artist);

        // Aggiorna artist sull'utente
        var user = userService.getEntityById(request.userId());
        user.setArtist(saved.getId());
        userService.saveEntity(user);

        return ArtistMapper.toResponse(saved);
    }

    public List<ArtistResponseDto> findAll(String name, String alias) {
        return artistRepository.findAll().stream()
                .filter(a -> !StringUtils.hasText(name)
                        || a.getFirstName().toLowerCase().contains(name.toLowerCase())
                        || a.getLastName().toLowerCase().contains(name.toLowerCase()))
                .filter(a -> !StringUtils.hasText(alias)
                        || (a.getAlias() != null && a.getAlias().toLowerCase().contains(alias.toLowerCase())))
                .map(ArtistMapper::toResponse)
                .toList();
    }

    public ArtistResponseDto findById(String id) {
        return ArtistMapper.toResponse(getEntityById(id));
    }

    public ArtistResponseDto update(String id, ArtistRequestDto request) {
        Artist artist = getEntityById(id);
        artist.setFirstName(request.firstName());
        artist.setLastName(request.lastName());
        artist.setAlias(request.alias());
        artist.setPiva(request.piva());
        artist.setPhone(request.phone());
        return ArtistMapper.toResponse(artistRepository.save(artist));
    }

    public void delete(String id) {
        Artist artist = getEntityById(id);
        artistRepository.delete(artist);

        // Rimuovi artist sull'utente
        userRepository.findById(id).ifPresent(user -> {
            user.setArtist(null);
            userRepository.save(user);
        });
    }

    public Artist getEntityById(String id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artista non trovato con id - " + id));
    }

    public void addReview(String artistId, String reviewId) {
        Artist artist = getEntityById(artistId);
        if (!artist.getReviews().contains(reviewId)) {
            artist.getReviews().add(reviewId);
            artistRepository.save(artist);
        }
    }

    public void removeReview(String artistId, String reviewId) {
        Artist artist = getEntityById(artistId);
        artist.getReviews().remove(reviewId);
        artistRepository.save(artist);
    }

    public void syncEventStatus(String artistId, String eventId, EventStatus status) {
        Artist artist = getEntityById(artistId);
        artist.getActiveEvents().remove(eventId);
        artist.getLastEvents().remove(eventId);
        if (status == EventStatus.ENDED) {
            artist.getLastEvents().add(eventId);
        } else {
            artist.getActiveEvents().add(eventId);
        }
        artistRepository.save(artist);
    }

    public void removeEventReferences(String artistId, String eventId) {
        Artist artist = getEntityById(artistId);
        artist.getActiveEvents().remove(eventId);
        artist.getLastEvents().remove(eventId);
        artistRepository.save(artist);
    }

    private void ensureUserExists(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id - " + userId));
    }
}

