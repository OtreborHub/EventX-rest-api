package com.eventx.api.controller;

import com.eventx.api.dto.ArtistRequestDto;
import com.eventx.api.dto.ArtistResponseDto;
import com.eventx.api.service.ArtistService;
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
@RequestMapping("/api/v1/artisti")
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping
    public ResponseEntity<ArtistResponseDto> create(@Valid @RequestBody ArtistRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ArtistResponseDto>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String alias) {
        return ResponseEntity.ok(artistService.findAll(name, alias));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponseDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(artistService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistResponseDto> update(@PathVariable String id,
                                                    @Valid @RequestBody ArtistRequestDto request) {
        return ResponseEntity.ok(artistService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        artistService.delete(id);
    }
}

