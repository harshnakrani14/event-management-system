package com.example.ems.repository;

import com.example.ems.model.Event;
import com.example.ems.model.Ticket;
import com.example.ems.util.exception.NotFoundException;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    List<Ticket> findByUserId(String userId);
    List<Ticket> findByEventId(String eventId);

    default Ticket findTicketByIdOrThrow(String ticketId) {
        return findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket", ticketId));
    }

    void deleteByEvent(Event event);
}
