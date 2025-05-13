package com.example.ems.mapper;

import com.example.ems.config.GlobalMapperConfig;
import com.example.ems.dto.TimingDto;
import com.example.ems.model.core.Timing;
import com.example.ems.util.DateUtil;
import com.example.ems.util.SecurityUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(config = GlobalMapperConfig.class, imports = {DateUtil.class, SecurityUtil.class})
public interface TimingMapper {

    TimingMapper INSTANCE = Mappers.getMapper(TimingMapper.class);

    @Mapping(target = "startTime", expression =
            "java(DateUtil.convertToUserTimeZone(timing.getStartTime(), SecurityUtil.getCurrentUser().getTimeZone()))")
    @Mapping(target = "endTime", expression =
            "java(DateUtil.convertToUserTimeZone(timing.getEndTime(), SecurityUtil.getCurrentUser().getTimeZone()))")
    TimingDto toDto(Timing timing);

    @Mapping(target = "timingId", ignore = true)
    @Mapping(target = "startTime", expression =
            "java(DateUtil.convertToUTC(timingDto.getStartTime(), SecurityUtil.getCurrentUser().getTimeZone()))")
    @Mapping(target = "endTime", expression =
            "java(DateUtil.convertToUTC(timingDto.getEndTime(), SecurityUtil.getCurrentUser().getTimeZone()))")
    Timing toEntity(TimingDto timingDto);

}