package com.eventx.api.service;

import com.eventx.api.dto.ChangePasswordRequestDto;
import com.eventx.api.dto.UpdateProfileRequestDto;
import com.eventx.api.dto.UserRequestDto;
import com.eventx.api.dto.UserResponseDto;
import com.eventx.api.exceptions.ConflictException;
import com.eventx.api.exceptions.ResourceNotFoundException;
import com.eventx.api.exceptions.UnauthorizedException;
import com.eventx.api.mapper.UserMapper;
import com.eventx.api.models.EventStatus;
import com.eventx.api.models.User;
import com.eventx.api.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto register(UserRequestDto request) {
        checkEmailConflict(request.email(), null);
        checkUsernameConflict(request.username(), null);

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .paypalEmail(request.paypalEmail() != null ? request.paypalEmail() : "")
                .isArtist(false)
                .isLocation(false)
                .activeTickets(new ArrayList<>())
                .lastTickets(new ArrayList<>())
                .reviews(new ArrayList<>())
                .registrationDate(LocalDateTime.now())
                .build();

        return UserMapper.toResponse(userRepository.save(user));
    }

    public List<UserResponseDto> findAll(String cerca) {
        return userRepository.findAll().stream()
                .filter(user -> !StringUtils.hasText(cerca)
                        || user.getUsername().toLowerCase().contains(cerca.toLowerCase()))
                .map(UserMapper::toResponse)
                .toList();
    }

    public UserResponseDto findById(String id) {
        return UserMapper.toResponse(getEntityById(id));
    }

    public UserResponseDto findByEmail(String email) {
        return UserMapper.toResponse(userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con email - " + email)));
    }

    public void deleteById(String id) {
        User user = getEntityById(id);
        userRepository.delete(user);
    }

    public UserResponseDto updateProfile(String id, UpdateProfileRequestDto request) {
        User user = getEntityById(id);
        ensureCurrentPassword(user, request.currentPassword());

        if (StringUtils.hasText(request.email()) && !request.email().equalsIgnoreCase(user.getEmail())) {
            checkEmailConflict(request.email(), user.getId());
            user.setEmail(request.email());
        }
        if (request.paypalEmail() != null) {
            user.setPaypalEmail(request.paypalEmail());
        }

        return UserMapper.toResponse(userRepository.save(user));
    }

    public void changePassword(String id, ChangePasswordRequestDto request) {
        User user = getEntityById(id);
        ensureCurrentPassword(user, request.currentPassword());
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    public User getEntityById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id - " + id));
    }

    public User saveEntity(User user) {
        return userRepository.save(user);
    }

    public void addActiveTicket(String userId, String ticketId) {
        User user = getEntityById(userId);
        if (!user.getActiveTickets().contains(ticketId)) {
            user.getActiveTickets().add(ticketId);
            userRepository.save(user);
        }
    }

    public void moveTicketToLast(String userId, String ticketId) {
        User user = getEntityById(userId);
        user.getActiveTickets().remove(ticketId);
        if (!user.getLastTickets().contains(ticketId)) {
            user.getLastTickets().add(ticketId);
        }
        userRepository.save(user);
    }

    public void removeTicketReferences(String userId, String ticketId) {
        User user = getEntityById(userId);
        user.getActiveTickets().remove(ticketId);
        user.getLastTickets().remove(ticketId);
        userRepository.save(user);
    }

    public void addReview(String userId, String reviewId) {
        User user = getEntityById(userId);
        if (!user.getReviews().contains(reviewId)) {
            user.getReviews().add(reviewId);
            userRepository.save(user);
        }
    }

    public void removeReview(String userId, String reviewId) {
        User user = getEntityById(userId);
        user.getReviews().remove(reviewId);
        userRepository.save(user);
    }

    private void ensureCurrentPassword(User user, String currentPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new UnauthorizedException("Credenziali non valide");
        }
    }

    private void checkEmailConflict(String email, String currentUserId) {
        userRepository.findByEmailIgnoreCase(email)
                .filter(existing -> !existing.getId().equals(currentUserId))
                .ifPresent(existing -> {
                    throw new ConflictException("Email già in uso");
                });
    }

    private void checkUsernameConflict(String username, String currentUserId) {
        userRepository.findByUsernameIgnoreCase(username)
                .filter(existing -> !existing.getId().equals(currentUserId))
                .ifPresent(existing -> {
                    throw new ConflictException("Username già in uso");
                });
    }
}
