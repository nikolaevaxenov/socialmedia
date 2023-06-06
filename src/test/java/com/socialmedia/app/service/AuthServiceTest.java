package com.socialmedia.app.service;

import com.socialmedia.app.dto.SignInRequest;
import com.socialmedia.app.dto.SignUpRequest;
import com.socialmedia.app.model.User;
import com.socialmedia.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private TokenService tokenService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(tokenService, authenticationManager, passwordEncoder, userRepository);
    }

    @Test
    void signIn_ValidCredentials_ReturnsToken() {
        // Arrange
        SignInRequest signInRequest = new SignInRequest("testUsername", "testPassword");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.generateToken(authentication)).thenReturn("testToken");

        // Act
        String token = authService.signIn(signInRequest);

        // Assert
        assertEquals("testToken", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(authentication);
    }

    @Test
    void signUp_ValidRequest_ReturnsToken() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest("testUsername", "testEmail", "testPassword");
        when(passwordEncoder.encode(signUpRequest.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User("testUsername", "testEmail", "encodedPassword"));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(tokenService.generateToken(any(Authentication.class))).thenReturn("testToken");

        // Act
        String token = authService.signUp(signUpRequest);

        // Assert
        assertEquals("testToken", token);
        verify(passwordEncoder).encode("testPassword");
        verify(userRepository).save(any(User.class));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(any(Authentication.class));
    }

    @Test
    void signUp_UserAlreadyExists_ThrowsException() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest("testUsername", "testEmail", "testPassword");
        when(passwordEncoder.encode(signUpRequest.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("User already exists"));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> authService.signUp(signUpRequest));
        verify(passwordEncoder).encode("testPassword");
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(authenticationManager, tokenService);
    }
}