package com.example.ems.mapper;

import com.example.ems.dto.EventDto;
import com.example.ems.model.Event;
import com.example.ems.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {LocationMapper.class})
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "organizerUsername", source = "organizer.name")
    EventDto toDto(Event event);

    List<EventDto> toDtoList(List<Event> event);

    @Mapping(target = "id", source = "eventDto.id")
    @Mapping(target = "organizer", source = "user")
    @Mapping(target = "location", source = "eventDto.location")
    @Mapping(target = "createdAt", source = "eventDto.createdAt")
    @Mapping(target = "modifiedAt", source = "eventDto.modifiedAt")
    Event toEntity(EventDto eventDto, User user);

    // For partial updates
    @Mapping(target = "id", ignore = true) // Prevent ID overwrite
    @Mapping(target = "organizer", ignore = true) // Handle separately
    void updateEventFromDto(EventDto dto, @MappingTarget Event entity);

}

