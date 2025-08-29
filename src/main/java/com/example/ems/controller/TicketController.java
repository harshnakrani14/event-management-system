package com.example.ems.controller;

import com.example.ems.dto.TicketDto;
import com.example.ems.dto.TicketResponseDto;
import com.example.ems.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Create a new ticket")
    public TicketDto createTicket(@Valid @RequestBody TicketDto ticketDto) {
        return ticketService.createTicket(ticketDto);
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Get tickets by event ID")
    public List<TicketDto> getTicketsByEvent(@PathVariable String eventId) {
        return ticketService.getTicketsByEvent(eventId);
    }

    @GetMapping("/user/{userId}")
//    @PreAuthorize("hasRole('USER')")
    @PreAuthorize("hasPermission(#userId, 'USER')")
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Get tickets by user ID")
    public List<TicketDto> getTicketsByUser(@PathVariable String userId) {
        return ticketService.getTicketsByUser(userId);
    }

    @DeleteMapping("/{ticketId}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Delete a ticket by ID")
    public void deleteTicket(@PathVariable String ticketId) {
        ticketService.deleteTicket(ticketId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Get all tickets")
    public List<TicketResponseDto> getTicketById() {
        return ticketService.getTicketDetailsById();
    }

}