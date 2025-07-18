package com.example.ems.controller;

import com.example.ems.dto.TicketDto;
import com.example.ems.dto.TicketResponseDto;
import com.example.ems.service.TicketService;
<<<<<<< HEAD
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
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
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Create a new ticket")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public TicketDto createTicket(@Valid @RequestBody TicketDto ticketDto) {
        return ticketService.createTicket(ticketDto);
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('USER')")
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Get tickets by event ID")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public List<TicketDto> getTicketsByEvent(@PathVariable String eventId) {
        return ticketService.getTicketsByEvent(eventId);
    }

    @GetMapping("/user/{userId}")
//    @PreAuthorize("hasRole('USER')")
    @PreAuthorize("hasPermission(#userId, 'USER')")
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Get tickets by user ID")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public List<TicketDto> getTicketsByUser(@PathVariable String userId) {
        return ticketService.getTicketsByUser(userId);
    }

    @DeleteMapping("/{ticketId}")
    @PreAuthorize("hasRole('USER')")
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Delete a ticket by ID")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public void deleteTicket(@PathVariable String ticketId) {
        ticketService.deleteTicket(ticketId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Get all tickets")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public List<TicketResponseDto> getTicketById() {
        return ticketService.getTicketDetailsById();
    }

}