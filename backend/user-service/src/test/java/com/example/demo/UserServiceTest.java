package com.example.demo;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.exception.ResourceNotFoundException;


import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private Authentication authentication;

    @InjectMocks private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldReturnUserDto_WhenSuccessful() {
        RegisterRequest request = new RegisterRequest("devashish", "devashish@example.com", "root", "ROLE_USER");

        when(userRepository.existsByEmail("devashish@example.com")).thenReturn(false);
        when(passwordEncoder.encode("root")).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("devashish");
        savedUser.setEmail("devashish@example.com");
        savedUser.setPassword("root");
        savedUser.setRole("ROLE_USER");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.registerUser(request);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        assertEquals("alice@example.com", result.getEmail());
        assertEquals("ROLE_USER", result.getRole());
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailExists() {
        RegisterRequest request = new RegisterRequest("Bob", "bob@example.com", "pass", "ROLE_USER");

        when(userRepository.existsByEmail("bob@example.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(request);
        });

        assertEquals("Email is already taken!", exception.getMessage());
    }

    @Test
    void authenticateUser_ShouldReturnLoginResponse_WhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("test@example.com", "password");

        User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("test@example.com");
        user.setPassword("encodedPass");
        user.setRole("ROLE_USER");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken("test@example.com", "ROLE_USER", 1L)).thenReturn("fake-jwt-token");

        LoginResponse response = userService.authenticateUser(request);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals(1L, response.getId());
        assertEquals("Test", response.getName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("ROLE_USER", response.getRole());
    }

    @Test
    void getUserById_ShouldReturnUserDto_WhenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@example.com");
        user.setRole("ROLE_USER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(100L));
    }
}
