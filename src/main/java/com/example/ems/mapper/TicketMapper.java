package com.example.ems.mapper;

import com.example.ems.config.GlobalMapperConfig;
import com.example.ems.dto.TicketDto;
import com.example.ems.dto.TicketResponseDto;
import com.example.ems.dto.TimingDto;
import com.example.ems.model.Event;
import com.example.ems.model.Ticket;
import com.example.ems.model.User;
import com.example.ems.util.DateUtil;
import com.example.ems.util.SecurityUtil;
import org.joda.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(config = GlobalMapperConfig.class, imports = {LocalDateTime.class, DateUtil.class, SecurityUtil.class}, uses = {LocationMapper.class})
public interface TicketMapper {

    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "ticketId", source = "id")
    @Mapping(target = "slotsRequested", source = "slotsBooked")
    @Mapping(target = "bookingTime", expression =
            "java(DateUtil.convertToUserTimeZone(ticket.getBookingTime(), SecurityUtil.getCurrentUser().getTimeZone()))")
    TicketDto toDto(Ticket ticket);

    @Mapping(target = "event", source = "event")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", source = "ticketDto.ticketId")
    @Mapping(target = "slotsBooked", source = "ticketDto.slotsRequested")
//    @Mapping(target = "bookingTime", defaultExpression = "java(LocalDateTime.now())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "bookingTime", expression =
            "java(DateUtil.convertToUTC(LocalDateTime.now()))")
    Ticket toEntity(TicketDto ticketDto, User user, Event event);

    @Mapping(target = "eventName", source = "ticket.event.eventName")
    @Mapping(target = "location", source = "ticket.event.location")
    @Mapping(target = "name", source = "ticket.user.name")
//    @Mapping(target = "bookingTime", source = "ticket.bookingTime")
    @Mapping(target = "slotsBooked", source = "ticket.slotsBooked")
    @Mapping(target = "timing.availableSlots", ignore = true)
    @Mapping(target = "bookingTime", expression =
            "java(DateUtil.convertToUserTimeZone(ticket.getBookingTime(), SecurityUtil.getCurrentUser().getTimeZone()))")
    TicketResponseDto map(TimingDto timing, Ticket ticket);

}