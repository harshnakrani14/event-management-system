package com.example.ems.service;

import com.example.ems.dto.EventDto;
import com.example.ems.dto.LocationDto;
import com.example.ems.mapper.EventMapper;
import com.example.ems.mapper.LocationMapper;
import com.example.ems.model.Event;
import com.example.ems.model.core.Location;
import com.example.ems.model.core.Timing;
import com.example.ems.model.User;
import com.example.ems.repository.EventRepository;
import com.example.ems.repository.TicketRepository;
import com.example.ems.repository.UserRepository;
import com.example.ems.util.SecurityUtil;
import com.example.ems.exception.CustomAccessDeniedException;
import com.example.ems.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final ValidationService validationService;

    @Transactional
    public EventDto createEvent(EventDto eventDto) {

        Event event = EventMapper.INSTANCE.toEntity(eventDto, SecurityUtil.getCurrentUser());
        event.setShowTime(validationService.convertAndValidateTimings(eventDto));
        validationService.checkForOverlappingEvents(event.getLocation(), event.getShowTime(), null);
        return EventMapper.INSTANCE.toDto(eventRepository.save(event));
    }

    public List<EventDto> getEventsByOrganizer(String name) {

        User organizer = userRepository.findByNameOrThrow(name);
        List<Event> events = eventRepository.findByOrganizer_id(organizer.getId());
        return EventMapper.INSTANCE.toDtoList(events);
    }

    public List<EventDto> getEventByLocation(LocationDto location) {

        return EventMapper.INSTANCE.toDtoList(
                eventRepository.findByLocation(
                        LocationMapper.INSTANCE.toEntity(location)
                )
        );
    }

    @Transactional
    public EventDto updateEvent(EventDto eventDto) {

        Event existingEvent = eventRepository.findEventByIdOrThrow(eventDto.getId());

        // Ensure only the organizer can update
        validateOrganizerAuthorization(existingEvent);

        EventMapper.INSTANCE.updateEventFromDto(eventDto, existingEvent);
        return EventMapper.INSTANCE.toDto(eventRepository.save(existingEvent));
    }

    public EventDto getEventDtoById(String eventId) {

        Event event = eventRepository.findEventByIdOrThrow(eventId);
        return EventMapper.INSTANCE.toDto(event);
    }

    @Transactional
    public void deleteEvent(String eventId) {

        Event event = eventRepository.findEventByIdOrThrow(eventId);
        String currentUser = SecurityUtil.getCurrentUser().getId();

        if (!currentUser.equals(event.getOrganizer().getId())) {
            throw new CustomAccessDeniedException("You are not allowed to delete events of other organizers.");
        }

        ticketRepository.deleteByEvent(event);
        eventRepository.delete(event);
    }

    private void validateOrganizerAuthorization(Event event) {

        if (!event.getOrganizer().getName().equals(SecurityUtil.getCurrentUser().getName())) {
            throw new CustomAccessDeniedException("Only the organizer can modify the event.");
        }
    }

    public List<EventDto> searchEventsByName(String eventName) {

        if (eventName == null || eventName.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String[] tokens = eventName.trim().split("\\s+");
        StringBuilder regexPattern = new StringBuilder();

        for (String token : tokens) {
            if (!token.trim().isEmpty()) {
                regexPattern.append("(?=.*").append(Pattern.quote(token)).append(")");
            }
        }

        regexPattern.append(".*");
        List<Event> matchingEvents = eventRepository.searchEvent(regexPattern.toString());

        if (matchingEvents.isEmpty()) {
            throw new NotFoundException("No search data found.");
        }

        return EventMapper.INSTANCE.toDtoList(matchingEvents);
    }


    public List<EventDto> getAllEvents() {

        Location userLocation = SecurityUtil.getCurrentUser().getLocation();

        if (userLocation == null) {
            throw new IllegalStateException("User location is not set. Please update your profile with location info.");
        }

        List<Event> nearbyEvents = eventRepository.findEventsNearLocation(userLocation.getLatitude(),
                userLocation.getLongitude()); //lat, lon
        return EventMapper.INSTANCE.toDtoList(nearbyEvents);
    }

    public Timing getTimingForEvent(String eventId, String timingId) {

        Event event = eventRepository.findEventByIdOrThrow(eventId);

        return event.getShowTime().stream()
                .filter(t -> t.getTimingId().equals(timingId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Timing", timingId));
    }

}