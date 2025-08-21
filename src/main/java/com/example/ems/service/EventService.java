package com.example.ems.service;

import com.example.ems.dto.EventDto;
import com.example.ems.dto.LocationDto;
import com.example.ems.model.Event;
import com.example.ems.model.core.Timing;

import java.util.List;

public interface EventService {

    EventDto createEvent(EventDto eventDto);

    List<EventDto> getEventsByOrganizer(String name);

    List<EventDto> getEventByLocation(LocationDto location);

    EventDto updateEvent(EventDto eventDto);

    EventDto getEventDtoById(String eventId);

    void deleteEvent(String eventId);

    void validateOrganizerAuthorization(Event event);

    List<EventDto> searchEventsByName(String eventName);

    List<EventDto> getAllEvents();

    Timing getTimingForEvent(String eventId, String timingId);
}
