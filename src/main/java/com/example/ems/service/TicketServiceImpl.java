package com.example.ems.service;

import com.example.ems.dto.TicketDto;
import com.example.ems.dto.TicketResponseDto;
import com.example.ems.mapper.TicketMapper;
import com.example.ems.mapper.TimingMapper;
import com.example.ems.model.Event;
import com.example.ems.model.Ticket;
import com.example.ems.model.core.Timing;
import com.example.ems.model.User;
import com.example.ems.repository.EventRepository;
import com.example.ems.repository.TicketRepository;
import com.example.ems.util.SecurityUtil;
import com.example.ems.exception.CustomAccessDeniedException;
import com.example.ems.exception.SlotConflictException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final EventServiceImpl eventServiceImpl;

    @SneakyThrows
    @Override
    @Transactional
    public TicketDto createTicket(TicketDto ticketDto) {

        User currentUser = SecurityUtil.getCurrentUser();
        Event event = eventRepository.findEventByIdOrThrow(ticketDto.getEventId());

        Timing selectTiming = eventServiceImpl.getTimingForEvent(event.getId(), ticketDto.getTimingId());

        for (Timing timing : event.getShowTime()) {
            if (timing.getTimingId().equals(selectTiming.getTimingId())) {

                if (timing.getAvailableSlots() < ticketDto.getSlotsRequested()) {
                    throw new SlotConflictException("Sorry, not enough slots.");
                }

                timing.setAvailableSlots(timing.getAvailableSlots() - ticketDto.getSlotsRequested());
                break;
            }
        }

        event.setCapacity(event.getCapacity() - ticketDto.getSlotsRequested());
        eventRepository.save(event);
        Ticket ticket = TicketMapper.INSTANCE.toEntity(ticketDto, currentUser, event);
        return TicketMapper.INSTANCE.toDto(ticketRepository.save(ticket));
    }

    @SneakyThrows
    @Override
    public List<TicketDto> getTicketsByEvent(String eventId) {

        return ticketRepository.findByEventId(eventId)
                .stream()
                .map(TicketMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public List<TicketDto> getTicketsByUser(String userId) {

        return ticketRepository.findByUserId(userId)
                .stream()
                .map(TicketMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    @Transactional
    public void deleteTicket(String ticketId) {

        String currentUser = SecurityUtil.getCurrentUser().getId();
        Ticket existingTicket = ticketRepository.findTicketByIdOrThrow(ticketId);

        if (!currentUser.equals(existingTicket.getUser().getId())) {
            throw new CustomAccessDeniedException("You can't delete others tickets.");
        }

        Event event = existingTicket.getEvent();
        int slots = existingTicket.getSlotsBooked();

        // Update timing inside the list
        for (Timing t : event.getShowTime()) {
            if (t.getTimingId().equals(existingTicket.getTimingId())) {
                t.setAvailableSlots(t.getAvailableSlots() + slots);
                break;
            }
        }

        event.setCapacity(event.getCapacity() + slots);
        eventRepository.save(event);
        ticketRepository.deleteById(ticketId);
    }

    @SneakyThrows
    @Override
    public List<TicketResponseDto> getTicketDetailsById() {

        String currentUser = SecurityUtil.getCurrentUser().getId();
        List<Ticket> tickets = ticketRepository.findByUserId(currentUser);

        return tickets.stream()
                .map(
                        ticket -> {
                            Timing selectedTiming = eventServiceImpl.getTimingForEvent(ticket.getEvent().getId(),
                                    ticket.getTimingId());
                            return TicketMapper.INSTANCE.map(TimingMapper.INSTANCE.toDto(selectedTiming), ticket);
                        }
                )
                .collect(Collectors.toList());
    }

}