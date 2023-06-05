package com.socialmedia.app.controller;

import com.socialmedia.app.dto.SignInRequest;
import com.socialmedia.app.dto.SignUpRequest;
import com.socialmedia.app.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling authorization-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/authorization")
@Tag(name = "Authorization", description = "Endpoints for user authentication and authorization")
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
    @Operation(summary = "Sign In")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successful sign-in"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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
    @Operation(summary = "Sign Up")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful sign-up"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public String signUp(@RequestBody SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }
}
