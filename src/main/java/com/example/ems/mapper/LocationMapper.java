package com.example.ems.mapper;

import com.example.ems.config.GlobalMapperConfig;
import com.example.ems.dto.LocationDto;
import com.example.ems.model.core.Location;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = GlobalMapperConfig.class)
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    Location toEntity(LocationDto locationDto);
    LocationDto toDto(Location location);

}