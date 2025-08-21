package com.example.ems.service;

import com.example.ems.dto.UserDto;
import com.example.ems.dto.response.JwtResponse;

import java.util.List;

public interface UserService {

    UserDto registerUser(UserDto userDto);

    JwtResponse login(String email, String rawPassword);

    UserDto updateUser(UserDto userDto);

    List<UserDto> getAllUsers();

    void deleteUser(String id);

    UserDto getProfile();

}
