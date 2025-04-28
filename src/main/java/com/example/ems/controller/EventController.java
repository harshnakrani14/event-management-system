package com.example.ems.controller;

import com.example.ems.dto.EventDto;
import com.example.ems.dto.LocationDto;
import com.example.ems.dto.TimingDto;
import com.example.ems.service.EventService;
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
    @PreAuthorize("hasRole('ADMIN')")
    public EventDto createEvent(@RequestBody EventDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    @GetMapping("/all-events")
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    public List<EventDto> getAllEvent() {
        return eventService.getAllEvents();
    }

    @GetMapping("/organizer/{username}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public List<EventDto> getEventsByOrganizer(@PathVariable String username) {
        return eventService.getEventsByOrganizer(username);
    }

    @GetMapping("/location")
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    public List<EventDto> getEventsByLocation(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        return eventService.getEventByLocation(new LocationDto(latitude, longitude));
    }


    @GetMapping("/{eventId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public EventDto getEventById(@PathVariable String eventId) {
        return eventService.getEventDtoById(eventId);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public EventDto updateEvent(@RequestBody EventDto eventDto) {
        return eventService.updateEvent(eventDto);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEvent(@PathVariable String eventId) {
        eventService.deleteEvent(eventId);
    }

    // this is for get all timing details
    @GetMapping("{eventId}/timings/{timingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public TimingDto getTimingDetails(@PathVariable String eventId, @PathVariable String timingId) {
        return eventService.getTimingById(eventId, timingId);
    }

    @GetMapping("/search/{eventName}")
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    public List<EventDto> searchEvents(@PathVariable String eventName) {
        return eventService.searchEventsByName(eventName);
    }



}


