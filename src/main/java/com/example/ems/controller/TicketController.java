package com.example.ems.controller;

import com.example.ems.dto.TicketDto;
import com.example.ems.dto.TicketResponseDto;
import com.example.ems.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public TicketDto createTicket(@Valid @RequestBody TicketDto ticketDto) {
        return ticketService.createTicket(ticketDto);
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public List<TicketDto> getTicketsByEvent(@PathVariable String eventId) {
        return ticketService.getTicketsByEvent(eventId);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public List<TicketDto> getTicketsByUser(@PathVariable String userId) {
        return ticketService.getTicketsByUser(userId);
    }

    @DeleteMapping("/{ticketId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteTicket(@PathVariable String ticketId) {
        ticketService.deleteTicket(ticketId);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public TicketDto updateTicket(@Valid @RequestBody TicketDto ticketDto) {
        return ticketService.updateTicket(ticketDto);
    }

    @GetMapping("/{ticketId}")
    @PreAuthorize("hasRole('USER')")
    public TicketResponseDto getTicketById(@PathVariable String ticketId) {
        return ticketService.getTicketDetailsById(ticketId);
    }
}



