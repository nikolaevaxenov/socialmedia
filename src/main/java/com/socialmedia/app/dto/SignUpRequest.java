package com.socialmedia.app.dto;

/**
 * Represents a sign-up request containing the username, email, and password.
 */
public record SignUpRequest(String username, String email, String password) {
}
