package com.example.ems.mapper;

import com.example.ems.dto.TimingDto;
import com.example.ems.model.Timing;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TimingMapper {
    TimingMapper INSTANCE = Mappers.getMapper(TimingMapper.class);
    TimingDto toDto(Timing timing);
    Timing toEntity(TimingDto timingDto);

}
