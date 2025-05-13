package com.example.ems.controller;

import com.example.ems.dto.UserDto;
import com.example.ems.dto.request.JwtRequest;
import com.example.ems.dto.response.JwtResponse;
import com.example.ems.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // Register User
    @PostMapping("/register")
    public UserDto registerUser(@Valid @RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }

    //login controller with jwt token
    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PatchMapping("/update")
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/profile")
    public UserDto getMyDetails() {
        return userService.getProfile();
    }

    @DeleteMapping("/delete/user/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

}