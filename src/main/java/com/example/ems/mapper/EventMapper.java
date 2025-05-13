package com.example.ems.mapper;

import com.example.ems.config.GlobalMapperConfig;
import com.example.ems.dto.EventDto;
import com.example.ems.dto.TimingDto;
import com.example.ems.model.Event;
import com.example.ems.model.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class, uses = {LocationMapper.class, TimingMapper.class})
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "organizerUsername", source = "organizer.name")
    EventDto toDto(Event event);

    List<EventDto> toDtoList(List<Event> event);

    @Mapping(target = "id", source = "eventDto.id")
    @Mapping(target = "organizer", source = "user")
    @Mapping(target = "location", source = "eventDto.location")
    @Mapping(target = "capacity", source = "eventDto.showTime", qualifiedByName = "calculateCapacity")
    Event toEntity(EventDto eventDto, User user);

    // For partial updates
    @Mapping(target = "id", ignore = true) // Prevent ID overwrite
    @Mapping(target = "organizer", ignore = true) // Handle separately
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "capacity", source = "dto.showTime", qualifiedByName = "calculateCapacity")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    void updateEventFromDto(EventDto dto, @MappingTarget Event entity);

    @Named("calculateCapacity")
    default int calculateCapacity(List<TimingDto> timings) {

        return timings.stream()
                .mapToInt(TimingDto::getAvailableSlots)
                .sum();
    }

}