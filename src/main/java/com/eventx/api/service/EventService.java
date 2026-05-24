package com.eventx.api.service;

import com.eventx.api.dto.EventRequestDto;
import com.eventx.api.dto.EventResponseDto;
import com.eventx.api.dto.EventUpdateStatusRequestDto;
import com.eventx.api.exceptions.ResourceNotFoundException;
import com.eventx.api.mapper.EventMapper;
import com.eventx.api.models.Event;
import com.eventx.api.models.EventStatus;
import com.eventx.api.repository.EventRepository;
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
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final UserService userService;

    public EventResponseDto create(EventRequestDto request) {
        ensureUserExists(request.plannerId());
        //TODO INSERIRE VALIDAZIONE LOCATION
        //ensureLocationExists(request.locationId());

        Event event = Event.builder()
                .plannerId(request.plannerId())
                .name(request.name())
                .description(request.description())
                .locationId(request.locationId())
                .date(request.date())
                .duration(request.duration())
                .price(request.price())
                .capacity(request.capacity())
                .expectedPublic(0)
                .status(EventStatus.PUBLISHED)
                .reviews(new ArrayList<>())
                .registrationDate(LocalDateTime.now())
                .build();

        Event saved = eventRepository.save(event);
        return EventMapper.toResponse(saved);
    }

    public List<EventResponseDto> findAll(EventStatus status, String name, String plannerId, String locationId,
                                          LocalDateTime dal, LocalDateTime al) {
        return eventRepository.findAll().stream()
                .filter(event -> status == null || event.getStatus() == status)
                .filter(event -> !StringUtils.hasText(name)
                        || event.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(event -> !StringUtils.hasText(plannerId) || plannerId.equals(event.getPlannerId()))
                .filter(event -> !StringUtils.hasText(locationId) || locationId.equals(event.getLocationId()))
                .filter(event -> dal == null || !event.getDate().isBefore(dal))
                .filter(event -> al == null || !event.getDate().isAfter(al))
                .map(EventMapper::toResponse)
                .toList();
    }

    public EventResponseDto findById(String id) {
        return EventMapper.toResponse(getEntityById(id));
    }

    public EventResponseDto update(String id, EventUpdateStatusRequestDto request) {
        Event event = getEntityById(id);
        EventStatus oldStatus = event.getStatus();

        ensureUserExists(request.plannerId());
        ensureLocationExists(request.locationId());

        event.setPlannerId(request.plannerId());
        event.setName(request.name());
        event.setDescription(request.description());
        event.setLocationId(request.locationId());
        event.setDate(request.date());
        event.setDuration(request.duration());
        event.setPrice(request.price());
        event.setCapacity(request.capacity());
        event.setStatus(request.status() != null ? request.status() : oldStatus);

        Event saved = eventRepository.save(event);
        return EventMapper.toResponse(saved);
    }

    public void delete(String id) {
        Event event = getEntityById(id);
        eventRepository.delete(event);
    }

    public EventResponseDto updateStatus(String id, EventStatus status) {
        Event event = getEntityById(id);
        event.setStatus(status);
        Event saved = eventRepository.save(event);
        return EventMapper.toResponse(saved);
    }

    public Event getEntityById(String id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento non trovato con id - " + id));
    }

    public Event saveEntity(Event event) {
        return eventRepository.save(event);
    }

    public void addReview(String eventId, String reviewId) {
        Event event = getEntityById(eventId);
        if (!event.getReviews().contains(reviewId)) {
            event.getReviews().add(reviewId);
            eventRepository.save(event);
        }
    }

    public void removeReview(String eventId, String reviewId) {
        Event event = getEntityById(eventId);
        event.getReviews().remove(reviewId);
        eventRepository.save(event);
    }

    private void ensureUserExists(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id - " + userId));
    }

    private void ensureLocationExists(String locationId) {
        locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location non trovata con id - " + locationId));
    }
}
