package com.socialmedia.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    private final JwtEncoder jwtEncoder = mock(JwtEncoder.class);
    private final TokenService tokenService = new TokenService(jwtEncoder);

    @Test
    void generateToken_ValidAuthentication_ReturnsGeneratedToken() {
        // Arrange
        String username = "user1";
        String authority = "ROLE_USER";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                Collections.singleton(() -> authority));

        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.HOURS);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiration)
                .subject(username)
                .claim("scope", authority)
                .build();

        Jwt jwt = mock(Jwt.class); // Mock the Jwt class

        when(jwt.getTokenValue()).thenReturn("generated-token"); // Mock getTokenValue() to return a non-null value
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt); // Return the mocked Jwt object

        // Act
        String result = tokenService.generateToken(authentication);

        // Assert
        assertNotNull(result);
        verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
        verifyNoMoreInteractions(jwtEncoder);
    }
}