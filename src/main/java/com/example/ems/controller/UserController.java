package com.example.ems.controller;

import com.example.ems.dto.UserDto;
import com.example.ems.dto.request.JwtRequest;
import com.example.ems.dto.response.JwtResponse;
import com.example.ems.service.UserService;
<<<<<<< HEAD
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
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
<<<<<<< HEAD
    @Operation(summary = "Register a new user")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    @PostMapping("/register")
    public UserDto registerUser(@Valid @RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }

    //login controller with jwt token
    @PostMapping("/login")
<<<<<<< HEAD
    @Operation(summary = "Login user")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public JwtResponse login(@RequestBody JwtRequest loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PatchMapping("/update")
<<<<<<< HEAD
    @Operation(summary = "Update user details")
    @SecurityRequirement(name = "Bearer Token")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @GetMapping("/users")
    @Operation(summary = "Get all users")
=======
    @GetMapping("/users")
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "User profile details")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    @GetMapping("/profile")
    public UserDto getMyDetails() {
        return userService.getProfile();
    }

    @DeleteMapping("/delete/user/{id}")
<<<<<<< HEAD
    @SecurityRequirement(name = "Bearer Token")
    @Operation(summary = "Delete a user by ID")
=======
>>>>>>> 4b5862a1090311a5d42656c6413cc2b3a7a1d57b
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

}