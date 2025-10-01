package com.example.challenge.service;

import com.example.challenge.domain.User;
import com.example.challenge.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder; // Use the interface
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Use the interface for good practice

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // This method correctly expects a RAW password and encodes it.
    public User create(String fullName, String email, String rawPassword, boolean isAdmin) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(isAdmin ? "ROLE_ADMIN" : "ROLE_USER");
        return userRepository.save(user);
    }
}