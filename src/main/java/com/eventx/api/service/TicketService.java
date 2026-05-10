package com.eventx.api.service;

import com.eventx.api.dto.TicketBuyRequestDto;
import com.eventx.api.dto.TicketResponseDto;
import com.eventx.api.exceptions.BadRequestException;
import com.eventx.api.exceptions.ResourceNotFoundException;
import com.eventx.api.mapper.TicketMapper;
import com.eventx.api.models.Ticket;
import com.eventx.api.repository.TicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final EventService eventService;

    public TicketResponseDto buy(TicketBuyRequestDto request) {
        userService.getEntityById(request.userId());
        eventService.getEntityById(request.eventId());

        Ticket ticket = Ticket.builder()
                .position(request.position())
                .userId(request.userId())
                .eventId(request.eventId())
                .isBlockchain(false)
                .isWinner(false)
                .hash("")
                .registrationDate(LocalDateTime.now())
                .build();

        Ticket saved = ticketRepository.save(ticket);
        userService.addActiveTicket(saved.getUserId(), saved.getId());
        return TicketMapper.toResponse(saved);
    }

    public TicketResponseDto exchange(String id, String targetUserId) {
        Ticket ticket = getEntityById(id);
        userService.getEntityById(targetUserId);
        String currentOwnerId = ticket.getUserId();

        ticket.setUserId(targetUserId);
        Ticket saved = ticketRepository.save(ticket);

        userService.moveTicketToLast(currentOwnerId, saved.getId());
        userService.addActiveTicket(targetUserId, saved.getId());
        return TicketMapper.toResponse(saved);
    }

    public TicketResponseDto findById(String id) {
        return TicketMapper.toResponse(getEntityById(id));
    }

    public void delete(String id) {
        Ticket ticket = getEntityById(id);
        userService.removeTicketReferences(ticket.getUserId(), ticket.getId());
        ticketRepository.delete(ticket);
    }

    public List<TicketResponseDto> findAll(String userId, String eventId) {
        if (!StringUtils.hasText(userId) && !StringUtils.hasText(eventId)) {
            throw new BadRequestException("Almeno uno tra userId e eventId deve essere valorizzato");
        }
        return ticketRepository.findAll().stream()
                .filter(ticket -> !StringUtils.hasText(userId) || userId.equals(ticket.getUserId()))
                .filter(ticket -> !StringUtils.hasText(eventId) || eventId.equals(ticket.getEventId()))
                .map(TicketMapper::toResponse)
                .toList();
    }

    public Ticket getEntityById(String id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Biglietto non trovato con id - " + id));
    }
}
