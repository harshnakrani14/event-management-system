package com.example.ems.repository;

import com.example.ems.model.Event;
import com.example.ems.model.core.Location;
import com.example.ems.exception.NotFoundException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {

    List<Event> findByOrganizer_id(String organizerId);
    List<Event> findByLocation(Location location);
    List<Event> findByLocationAndIdNot(Location location, String id);

    @Query("{ 'eventName': { $regex: ?0, $options: 'i' } }")
    List<Event> searchEvent(String eventName);

    default Event findEventByIdOrThrow(String eventId) {
        return findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event", eventId));
    }

    @Query("{ 'location': { $near: { $geometry: { type: 'Point', coordinates: [?1, ?0] } } } }")
    List<Event> findEventsNearLocation(Double latitude, Double longitude);

}