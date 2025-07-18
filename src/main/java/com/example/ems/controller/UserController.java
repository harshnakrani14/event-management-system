package com.example.ems.controller;

import com.example.ems.dto.UserDto;
import com.example.ems.dto.request.JwtRequest;
import com.example.ems.dto.response.JwtResponse;
import com.example.ems.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public UserDto registerUser(@Valid @RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }

    //login controller with jwt token
    @PostMapping("/login")
    @Operation(summary = "Login user")
    public JwtResponse login(@RequestBody JwtRequest loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PatchMapping("/update")
    @Operation(summary = "Update user details")
    @SecurityRequirement(name = "Bearer Token")
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @SecurityRequirement(name = "Bearer Token")
    @GetMapping("/users")
    @Operation(summary = "Get all users")
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "User profile details")
    @GetMapping("/profile")
    public UserDto getMyDetails() {
        return userService.getProfile();
    }

    @DeleteMapping("/delete/user/{id}")
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Delete a user by ID")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

}