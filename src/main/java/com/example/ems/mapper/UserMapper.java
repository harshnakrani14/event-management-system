package com.example.ems.mapper;

import com.example.ems.config.GlobalMapperConfig;
import com.example.ems.dto.UserDto;
import com.example.ems.model.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class, uses = {LocationMapper.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "timeZone", expression = "java(com.example.ems.util.DateUtil.getTimeZoneFromLocation(userDto.getLocation()))")
    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);
    List<UserDto> toDtoList(List<User> user);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateUserFromDto(UserDto userDto, @MappingTarget User user);

}