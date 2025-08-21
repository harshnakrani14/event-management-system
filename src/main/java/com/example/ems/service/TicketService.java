package com.example.ems.service;

import com.example.ems.dto.TicketDto;
import com.example.ems.dto.TicketResponseDto;

import java.util.List;

public interface TicketService {

    TicketDto createTicket(TicketDto ticketDto);

    List<TicketDto> getTicketsByEvent(String eventId);

    List<TicketDto> getTicketsByUser(String userId);

    void deleteTicket(String ticketId);

    List<TicketResponseDto> getTicketDetailsById();
}
