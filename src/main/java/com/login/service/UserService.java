package com.login.service;

import com.login.dto.CreateStaffRequest;
import com.login.dto.RegisterRequest;
import com.login.model.Role;
import com.login.model.User;
import com.login.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new customer using email and password only.
     * Username is auto-generated from the email prefix.
     */
    public User registerCustomer(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Auto-generate username from email (take part before @, append suffix if needed)
        String baseUsername = request.getEmail().substring(0, request.getEmail().indexOf('@'));
        String username = baseUsername;
        int suffix = 1;
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        User user = new User(
                username,
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                null,
                Role.CUSTOMER
        );

        return userRepository.save(user);
    }

    /**
     * Create a staff user (admin operation).
     */
    public User createStaff(CreateStaffRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFullName() != null ? request.getFullName() : request.getUsername(),
                Role.STAFF
        );

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public long getUserCount() {
        return userRepository.count();
    }

    public long getUserCountByRole(Role role) {
        return userRepository.countByRole(role);
    }

    /**
     * Update user's role (admin operation).
     */
    public User updateUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(newRole);
        return userRepository.save(user);
    }

    /**
     * Toggle user enabled/disabled status (admin operation).
     */
    public User toggleUserEnabled(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEnabled(!user.isEnabled());
        return userRepository.save(user);
    }

    /**
     * Delete a user (admin operation).
     */
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(userId);
    }
}
