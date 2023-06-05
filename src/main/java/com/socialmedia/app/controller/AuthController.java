package com.socialmedia.app.controller;

import com.socialmedia.app.dto.SignInRequest;
import com.socialmedia.app.dto.SignUpRequest;
import com.socialmedia.app.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling authorization-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/authorization")
public class AuthController {
    private final AuthService authService;

    /**
     * Constructs the AuthController class.
     *
     * @param authService the AuthService implementation.
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handles the sign-in endpoint.
     *
     * @param signInRequest the SignInRequest object containing sign-in credentials.
     * @return the authentication token.
     */
    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String signIn(@RequestBody SignInRequest signInRequest) {
        return authService.signIn(signInRequest);
    }

    /**
     * Handles the sign-up endpoint.
     *
     * @param signUpRequest the SignUpRequest object containing sign-up details.
     * @return the created user's identifier.
     */
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public String signUp(@RequestBody SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }
}
