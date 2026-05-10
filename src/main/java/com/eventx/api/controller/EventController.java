package com.eventx.api.controller;

import com.eventx.api.dto.EventRequestDto;
import com.eventx.api.dto.EventResponseDto;
import com.eventx.api.models.EventStatus;
import com.eventx.api.service.EventService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/v1/eventi")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDto> create(@Valid @RequestBody EventRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> findAll(
            @RequestParam(required = false) EventStatus status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String creationId,
            @RequestParam(required = false) String locationId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime al) {
        return ResponseEntity.ok(eventService.findAll(status, name, creationId, locationId, dal, al));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> update(@PathVariable String id,
                                                   @Valid @RequestBody EventRequestDto request) {
        return ResponseEntity.ok(eventService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        eventService.delete(id);
    }

    @PatchMapping("/{id}/stato")
    public ResponseEntity<EventResponseDto> updateStatus(@PathVariable String id,
                                                         @RequestParam EventStatus status) {
        return ResponseEntity.ok(eventService.updateStatus(id, status));
    }
}
