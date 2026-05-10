package com.eventx.api.mapper;

import com.eventx.api.dto.TicketResponseDto;
import com.eventx.api.models.Ticket;

public final class TicketMapper {

    private TicketMapper() {
    }

    public static TicketResponseDto toResponse(Ticket ticket) {
        return new TicketResponseDto(
                ticket.getId(),
                ticket.getPosition(),
                ticket.getUserId(),
                ticket.getEventId(),
                ticket.isBlockchain(),
                ticket.isWinner(),
                ticket.getHash(),
                ticket.getRegistrationDate()
        );
    }
}
