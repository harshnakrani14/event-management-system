package com.example.ems.mapper;

import com.example.ems.dto.LocationDto;
import com.example.ems.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    Location toEntity(LocationDto locationDto);

    LocationDto toDto(Location location);
}
