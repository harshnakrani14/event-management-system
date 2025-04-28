package com.example.ems.service;

import com.example.ems.dto.JwtResponse;
import com.example.ems.dto.UserDto;
import com.example.ems.mapper.UserMapper;
import com.example.ems.model.User;
import com.example.ems.repository.UserRepository;
import com.example.ems.security.JwtService;
import com.example.ems.util.SecurityUtil;
import com.example.ems.util.exception.CustomAccessDeniedException;
import com.example.ems.util.exception.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserDto registerUser(UserDto userDto) {

        User email = userRepository.findByEmail(userDto.getEmail());

        if (email != null) {
            throw new EmailAlreadyExistsException("Email already in use: " + userDto.getEmail());
        }
        User user = UserMapper.INSTANCE.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return UserMapper.INSTANCE.userToUserDto(userRepository.save(user));
    }

    public JwtResponse login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password, please check your credentials.");
        }
        return new JwtResponse(jwtService.generateToken(user));
    }

    @Transactional
    public UserDto updateUser(UserDto userDto) {
        String currentUser = SecurityUtil.getCurrentUserEmail().getId();

        if (!currentUser.equals(userDto.getId())) {
            throw new CustomAccessDeniedException("Access denied: You can only update your own profile.");
        }
        User existingUser = userRepository.findUserByIdOrThrow(userDto.getId());
        UserMapper.INSTANCE.updateUserFromDto(userDto, existingUser);

        // If password is provided, encode it
        if (userDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        User updatedUser = userRepository.save(existingUser);
        return UserMapper.INSTANCE.userToUserDto(updatedUser);
    }

    // Get user by username (for authentication)
    public UserDto getUserByEmail(String email) {
        String currentUser = SecurityUtil.getCurrentUserEmail().getEmail();
        if (!currentUser.equals(email)) {
            throw new CustomAccessDeniedException("Access denied: You can only access your own data.");
        }
        User user = userRepository.findByEmail(email);
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    public UserDto getUserById(String id) {
        String currentUser = SecurityUtil.getCurrentUserEmail().getId();
        if (!currentUser.equals(id)) {
            throw new CustomAccessDeniedException("Access denied: You can only access your own data.");
        }
        User user = userRepository.findUserByIdOrThrow(id);
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return UserMapper.INSTANCE.toDtoList(userRepository.findAll());
    }

    public void deleteUser(String id) {
        String currentUser = SecurityUtil.getCurrentUserEmail().getId();
        if (!currentUser.equals(id)) {
            throw new CustomAccessDeniedException("Access denied: You can only delete your own account.");
        }
        userRepository.delete(userRepository.findUserByIdOrThrow(id));
    }

    public UserDto getProfile() {
        return UserMapper.INSTANCE.userToUserDto(SecurityUtil.getCurrentUserEmail());
    }
}

