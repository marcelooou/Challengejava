package com.example.challenge.service;

import com.example.challenge.domain.User;
import com.example.challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void testCreateUser_ShouldSaveWithRoleUser() {
        String rawPassword = "123456";

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L); // Simula ID gerado pelo BD
            return u;
        });

        User user = userService.create("Fulano", "fulano@email.com", rawPassword, false);

        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertEquals("Fulano", saved.getFullName());
        assertEquals("fulano@email.com", saved.getEmail());
        assertTrue(passwordEncoder.matches(rawPassword, saved.getPasswordHash()));
        assertEquals("ROLE_USER", saved.getRole());
        assertNotNull(user.getId());
    }

    @Test
    void testCreateAdmin_ShouldSaveWithRoleAdmin() {
        String rawPassword = "admin123";

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(2L);
            return u;
        });

        User admin = userService.create("Admin", "admin@email.com", rawPassword, true);

        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertEquals("Admin", saved.getFullName());
        assertEquals("admin@email.com", saved.getEmail());
        assertTrue(passwordEncoder.matches(rawPassword, saved.getPasswordHash()));
        assertEquals("ROLE_ADMIN", saved.getRole());
        assertNotNull(admin.getId());
    }

    @Test
    void testFindByEmail_ShouldReturnUser() {
        User u = new User();
        u.setEmail("teste@email.com");
        when(userRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(u));

        Optional<User> found = userService.findByEmail("teste@email.com");

        assertTrue(found.isPresent());
        assertEquals("teste@email.com", found.get().getEmail());
    }
}
