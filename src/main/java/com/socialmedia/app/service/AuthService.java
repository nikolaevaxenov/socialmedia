package com.socialmedia.app.service;

import com.socialmedia.app.dto.SignInRequest;
import com.socialmedia.app.dto.SignUpRequest;
import com.socialmedia.app.model.User;
import com.socialmedia.app.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling user authentication and registration.
 */
@Service
public class AuthService {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(TokenService tokenService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Authenticates a user with the provided sign-in request.
     *
     * @param signInRequest the sign-in request containing the username and password
     * @return the authentication token
     * @throws AuthenticationException if the authentication fails
     */
    public String signIn(SignInRequest signInRequest) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.username(), signInRequest.password()));
        return tokenService.generateToken(authentication);
    }

    /**
     * Registers a new user with the provided sign-up request.
     *
     * @param signUpRequest the sign-up request containing the username, email, and password
     * @return the authentication token
     * @throws DataIntegrityViolationException if a user with the same username or email already exists
     */
    public String signUp(SignUpRequest signUpRequest) {
        try {
            var newUser = new User(signUpRequest.username(), signUpRequest.email(), passwordEncoder.encode(signUpRequest.password()));
            userRepository.save(newUser);

            return signIn(new SignInRequest(signUpRequest.username(), signUpRequest.password()));
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("User with this username or email already exists!");
        }
    }
}
