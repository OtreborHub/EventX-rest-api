package com.eventx.api.controller;

import com.eventx.api.dto.TicketBuyRequestDto;
import com.eventx.api.dto.TicketResponseDto;
import com.eventx.api.service.TicketService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/buy")
    public ResponseEntity<TicketResponseDto> buy(@Valid @RequestBody TicketBuyRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.buy(request));
    }

    @PatchMapping("/{id}/exchange")
    public ResponseEntity<TicketResponseDto> exchange(@PathVariable String id,
                                                      @RequestParam String targetUserId) {
        return ResponseEntity.ok(ticketService.exchange(id, targetUserId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(ticketService.findById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        ticketService.delete(id);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDto>> findAll(@RequestParam(required = false) String userId,
                                                           @RequestParam(required = false) String eventId) {
        return ResponseEntity.ok(ticketService.findAll(userId, eventId));
    }
}
