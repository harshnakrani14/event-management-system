package com.example.ems.service;

import com.example.ems.dto.EventDto;
import com.example.ems.dto.LocationDto;
import com.example.ems.dto.TimingDto;
import com.example.ems.mapper.EventMapper;
import com.example.ems.mapper.LocationMapper;
import com.example.ems.mapper.TimingMapper;
import com.example.ems.model.Event;
import com.example.ems.model.Location;
import com.example.ems.model.Timing;
import com.example.ems.model.User;
import com.example.ems.repository.EventRepository;
import com.example.ems.repository.TicketRepository;
import com.example.ems.repository.UserRepository;
import com.example.ems.util.SecurityUtil;
import com.example.ems.util.exception.CustomAccessDeniedException;
import com.example.ems.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final ValidationService validationService;

    @Transactional
    public EventDto createEvent(EventDto eventDto) {

        User organizer = SecurityUtil.getCurrentUserEmail();
        Event event = EventMapper.INSTANCE.toEntity(eventDto, organizer);

        validateAndSetTimings(event, eventDto);
        validationService.checkForOverlappingEvents(event.getLocation(), event.getShowTime(), null);

        event.setCapacity(calculateCapacity(event.getShowTime()));
        event.setOrganizer(organizer);

        return EventMapper.INSTANCE.toDto(eventRepository.save(event));
    }

    public List<EventDto> getEventsByOrganizer(String username) {
        String currentUser = SecurityUtil.getCurrentUserEmail().getName();
        if (!currentUser.equals(username)) {
            throw new CustomAccessDeniedException("You are not allowed to view events of other organizers.");
        }
        User organizer = validateOrganizer(username);
        List<Event> events = eventRepository.findByOrganizer_id(organizer.getId());
        return EventMapper.INSTANCE.toDtoList(events);
    }

    public List<EventDto> getEventByLocation(LocationDto location) {
        List<Event> events = eventRepository.findByLocation(LocationMapper.INSTANCE.toEntity(location));
        return EventMapper.INSTANCE.toDtoList(events);
    }

    @Transactional
    public EventDto updateEvent(EventDto eventDto) {
        String currentUser = SecurityUtil.getCurrentUserEmail().getName();
        if (!currentUser.equals(eventDto.getOrganizerUsername())) {
            throw new CustomAccessDeniedException("You are not allowed to update events of other organizers.");
        }
        Event existingEvent = getExistingEvent(eventDto.getId());
        // Now safely check authorization
        validateOrganizerAuthorization(existingEvent, eventDto.getOrganizerUsername());

        // Update remaining fields (except organizer/id)
        EventMapper.INSTANCE.updateEventFromDto(eventDto, existingEvent);

        if (eventDto.getShowTime() != null) {
            validateAndSetTimings(existingEvent, eventDto);
            validationService.checkForOverlappingEvents(existingEvent.getLocation(), existingEvent.getShowTime(), eventDto.getId());
            existingEvent.setCapacity(calculateCapacity(existingEvent.getShowTime()));
        }
        return EventMapper.INSTANCE.toDto(eventRepository.save(existingEvent));
    }

    public EventDto getEventDtoById(String eventId) {
        Event event = getExistingEvent(eventId);
        return EventMapper.INSTANCE.toDto(event);
    }

    @Transactional
    public void deleteEvent(String eventId) {
        Event event = getExistingEvent(eventId);
        String currentUser = SecurityUtil.getCurrentUserEmail().getId();
        if (!currentUser.equals(event.getOrganizer().getId())) {
            throw new CustomAccessDeniedException("You are not allowed to delete events of other organizers.");
        }
        ticketRepository.deleteByEvent(event);
        eventRepository.delete(event);
    }

    // Helper methods
    private User validateOrganizer(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(name, " not found"));
    }

    private Event getExistingEvent(String eventId) {
        return eventRepository.findEventByIdOrThrow(eventId);
    }

    private void validateOrganizerAuthorization(Event event, String name) {
        if (!event.getOrganizer().getName().equals(name)) {
            throw new CustomAccessDeniedException("Only the organizer can modify the event.");
        }
    }

    private void validateAndSetTimings(Event event, EventDto eventDto) {
        List<Timing> timings = eventDto.getShowTime().stream()
                .map(TimingMapper.INSTANCE::toEntity)
                .collect(Collectors.toList());
        validationService.validateTimings(timings);
        event.setShowTime(timings);
    }

    // this is for see full timing details
    public TimingDto getTimingById(String eventId, String timingId) {
        Event event = eventRepository.findEventByIdOrThrow(eventId);
        return TimingMapper.INSTANCE.toDto(event.getShowTime().stream()
                .filter(t -> t.getTimingId().equals(timingId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Timing" ,timingId)));
    }

    public List<EventDto> searchEventsByName(String eventName) {
        if (eventName == null || eventName.trim().isEmpty()) {
            throw new IllegalArgumentException("Search text cannot be empty.");
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

    public int calculateCapacity(List<Timing> timings) {
        return timings.stream()
                .mapToInt(Timing::getAvailableSlots)
                .sum();
    }

    public List<EventDto> getAllEvents() {
        Location userLocation = SecurityUtil.getCurrentUserEmail().getLocation();
        if (userLocation == null) {
            throw new IllegalStateException("User location is not set. Please update your profile with location info.");
        }
        List<Event> nearbyEvents = eventRepository.findEventsNearLocation(userLocation.getLatitude(), userLocation.getLongitude()); // lat, lon
        return EventMapper.INSTANCE.toDtoList(nearbyEvents);
    }
}

