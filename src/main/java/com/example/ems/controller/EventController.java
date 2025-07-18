package com.example.ems.controller;

import com.example.ems.dto.EventDto;
import com.example.ems.dto.LocationDto;
import com.example.ems.service.EventService;
<<<<<<< HEAD
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Create a new event")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public EventDto createEvent(@RequestBody EventDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') || hasRole('ORGANIZER')")
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Get all events")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public List<EventDto> getAllEvent() {
        return eventService.getAllEvents();
    }

    @GetMapping("/organizer/{name}")
    @PreAuthorize("hasPermission(#name, 'ORGANIZER')")
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Get events by organizer name")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public List<EventDto> getEventsByOrganizer(@PathVariable String name) {
        return eventService.getEventsByOrganizer(name);
    }

    @GetMapping("/location")
    @PreAuthorize("hasRole('USER') || hasRole('ORGANIZER')")
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Get events by location")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public List<EventDto> getEventsByLocation(@RequestParam double latitude, @RequestParam double longitude) {
        return eventService.getEventByLocation(new LocationDto(latitude, longitude));
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER')")
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Get event by ID")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public EventDto getEventById(@PathVariable String eventId) {
        return eventService.getEventDtoById(eventId);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('ORGANIZER')")
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Update an existing event")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public EventDto updateEvent(@RequestBody EventDto eventDto) {
        return eventService.updateEvent(eventDto);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER')")
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Delete an event by ID")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public void deleteEvent(@PathVariable String eventId) {
        eventService.deleteEvent(eventId);
    }

    @GetMapping({"/search", "/search/{eventName}", "/search/"})
    @PreAuthorize("hasRole('USER') || hasRole('ORGANIZER')")
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Search events by name")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public List<EventDto> searchEvents(@PathVariable(required = false) String eventName) {
        return eventService.searchEventsByName(eventName);
    }

}