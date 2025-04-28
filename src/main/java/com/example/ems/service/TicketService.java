package com.example.ems.service;

import com.example.ems.dto.TicketDto;
import com.example.ems.dto.TicketResponseDto;
import com.example.ems.mapper.TicketMapper;
import com.example.ems.mapper.TimingMapper;
import com.example.ems.model.Event;
import com.example.ems.model.Ticket;
import com.example.ems.model.Timing;
import com.example.ems.model.User;
import com.example.ems.repository.EventRepository;
import com.example.ems.repository.TicketRepository;
import com.example.ems.util.SecurityUtil;
import com.example.ems.util.exception.CustomAccessDeniedException;
import com.example.ems.util.exception.NotFoundException;
import com.example.ems.util.exception.SlotConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;

    @Transactional
    public TicketDto createTicket(TicketDto ticketDto) {

        User currentUser = SecurityUtil.getCurrentUserEmail();
        Event event = eventRepository.findEventByIdOrThrow(ticketDto.getEventId());

        Timing selectTiming = event.getShowTime().stream()
                .filter(id -> id.getTimingId().equals(ticketDto.getTimingId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Timing", ticketDto.getTimingId()));

        if (selectTiming.getAvailableSlots() < ticketDto.getSlotsRequested()) {
            throw new SlotConflictException("Sorry, the selected slots are not available, Please select less slots");
        }
        selectTiming.setAvailableSlots(selectTiming.getAvailableSlots() - ticketDto.getSlotsRequested());
        event.setCapacity(event.getCapacity() - ticketDto.getSlotsRequested());

        eventRepository.save(event);
        Ticket ticket = TicketMapper.INSTANCE.createTicket(ticketDto, currentUser, event);
        ticket.setUser(currentUser);
        return TicketMapper.INSTANCE.toDto(ticketRepository.save(ticket));
    }

    public List<TicketDto> getTicketsByEvent(String eventId) {
        return ticketRepository.findByEventId(eventId)
                .stream()
                .map(TicketMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public List<TicketDto> getTicketsByUser(String userId) {
        String currentUser = SecurityUtil.getCurrentUserEmail().getId();
        if (!currentUser.equals(userId)) {
            throw new CustomAccessDeniedException("You can't view others tickets.");
        }
        return ticketRepository.findByUserId(userId)
                .stream()
                .map(TicketMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTicket(String ticketId) {
        String currentUser = SecurityUtil.getCurrentUserEmail().getId();
        Ticket existingTicket = ticketRepository.findTicketByIdOrThrow(ticketId);

        if (!currentUser.equals(existingTicket.getUser().getId())) {
            throw new CustomAccessDeniedException("You can't delete others tickets.");
        }
        Event event = existingTicket.getEvent();
        Timing timing = event.getShowTime().stream()
                        .filter(t -> t.getTimingId().equals(existingTicket.getTimingId()))
                                .findFirst()
                                        .orElseThrow(() -> new NotFoundException("Timing", existingTicket.getTimingId()));

        int slots = existingTicket.getSlotsBooked();
        timing.setAvailableSlots(timing.getAvailableSlots() + slots);
        event.setCapacity(event.getCapacity() + slots);
        eventRepository.save(event);
        ticketRepository.deleteById(ticketId);
    }

    @Transactional
    public TicketDto updateTicket(TicketDto ticketDto) {

        String currentUser = SecurityUtil.getCurrentUserEmail().getId();
        Ticket existingTicket = ticketRepository.findTicketByIdOrThrow(ticketDto.getTicketId());

        if (!currentUser.equals(existingTicket.getUser().getId())) {
            throw new CustomAccessDeniedException("You can't update others tickets.");
        }
        Event event = existingTicket.getEvent();
        Timing oldTiming = event.getShowTime().stream()
                .filter(t -> t.getTimingId().equals(existingTicket.getTimingId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Timing", existingTicket.getTimingId()));

        int oldSlots = existingTicket.getSlotsBooked();
        int newSlots = ticketDto.getSlotsRequested();
        int diff = newSlots - oldSlots;

        // If timing changed
        if (ticketDto.getTimingId() != null && !ticketDto.getTimingId().equals(existingTicket.getTimingId())) {
            Timing newTiming = event.getShowTime().stream()
                    .filter(t -> t.getTimingId().equals(ticketDto.getTimingId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Timing", ticketDto.getTimingId()));

            // Release old timing slots
            oldTiming.setAvailableSlots(oldTiming.getAvailableSlots() + oldSlots);

            // Check and book new timing slots
            if (newTiming.getAvailableSlots() < newSlots) {
                throw new SlotConflictException("Sorry, the selected slot is no longer available.");
            }
            newTiming.setAvailableSlots(newTiming.getAvailableSlots() - newSlots);
            existingTicket.setTimingId(ticketDto.getTimingId());
        } else {
            // Same timing, adjust available slots
            if (diff > 0 && oldTiming.getAvailableSlots() < diff) {
                throw new SlotConflictException("Sorry, the selected slot is no longer available.");
            }
            oldTiming.setAvailableSlots(oldTiming.getAvailableSlots() - diff);
        }
        // capacity update logic
        event.setCapacity(event.getCapacity() - diff); // diff = new - old, so subtraction works both ways
        eventRepository.save(event);
        // Update the ticket info
        existingTicket.setSlotsBooked(newSlots);

        if (ticketDto.getBookingTime() != null) {
            existingTicket.setBookingTime(ticketDto.getBookingTime());
    }
    User user = existingTicket.getUser();
    existingTicket.setUser(user);
    existingTicket.setUser(existingTicket.getUser());
    return TicketMapper.INSTANCE.toDto(ticketRepository.save(existingTicket));
}

    public TicketResponseDto getTicketDetailsById(String ticketId) {
        String currentUser = SecurityUtil.getCurrentUserEmail().getId();
        Ticket ticket = ticketRepository.findTicketByIdOrThrow(ticketId);

        if (!currentUser.equals(ticket.getUser().getId())) {
            throw new CustomAccessDeniedException("Sorry but you can't see others tickets.");
        }
        Timing selectedTiming = ticket.getEvent().getShowTime().stream()
                .filter(t -> t.getTimingId().equals(ticket.getTimingId()))
                .findFirst()
                .orElse(null);
        return TicketMapper.INSTANCE.map(TimingMapper.INSTANCE.toDto(selectedTiming),ticket);
    }
}
