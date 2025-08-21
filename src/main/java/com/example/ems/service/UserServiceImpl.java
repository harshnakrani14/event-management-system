package com.example.ems.service;

import com.example.ems.dto.UserDto;
import com.example.ems.dto.response.JwtResponse;
import com.example.ems.exception.CustomAccessDeniedException;
import com.example.ems.exception.UnauthorizedAccessException;
import com.example.ems.mapper.UserMapper;
import com.example.ems.model.Event;
import com.example.ems.model.User;
import com.example.ems.repository.EventRepository;
import com.example.ems.repository.TicketRepository;
import com.example.ems.repository.UserRepository;
import com.example.ems.security.JwtService;
import com.example.ems.util.SecurityUtil;
import com.example.ems.util.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @SneakyThrows
    public UserDto registerUser(UserDto userDto) {

        User user = UserMapper.INSTANCE.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return UserMapper.INSTANCE.userToUserDto(userRepository.save(user));
    }

    @Override
    @SneakyThrows
    public JwtResponse login(String email, String rawPassword) {

        User user = userRepository.findByEmail(email);

        if (user == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new UnauthorizedAccessException("Invalid email or password, please check your credentials.");
        }

        return new JwtResponse(jwtService.generateToken(user));
    }

    @Override
    @SneakyThrows
    @Transactional
    public UserDto updateUser(UserDto userDto) {

        User currentUser = SecurityUtil.getCurrentUser();
        UserMapper.INSTANCE.updateUserFromDto(userDto, currentUser);

        // If password is provided, encode it
        if (userDto.getPassword() != null) {
            currentUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        return UserMapper.INSTANCE.userToUserDto(userRepository.save(currentUser));
    }

    @Override
    @SneakyThrows
    public List<UserDto> getAllUsers() {

        return UserMapper.INSTANCE.toDtoList(userRepository.findAll());
    }

    @Override
    @SneakyThrows
    public void deleteUser(String id) {

        User currentUser = SecurityUtil.getCurrentUser();

        if (!currentUser.getId().equals(id)) {
            throw new CustomAccessDeniedException("Access denied: You can only delete your own account.");
        }

        if (currentUser.getRole() == Role.ORGANIZER) {

            List<Event> events = eventRepository.findByOrganizer_id(currentUser.getId());

            boolean hasTickets = events.stream()
                    .anyMatch(event -> !ticketRepository.findByEventId(event.getId()).isEmpty());

            if (hasTickets) {
                throw new CustomAccessDeniedException("Access denied: Organizer cannot be " +
                        "deleted because they have purchased tickets for their events.");
            }
        }

        userRepository.delete(userRepository.findUserByIdOrThrow(id));
    }

    @Override
    @SneakyThrows
    public UserDto getProfile() {

        return UserMapper.INSTANCE.userToUserDto(SecurityUtil.getCurrentUser());
    }

}