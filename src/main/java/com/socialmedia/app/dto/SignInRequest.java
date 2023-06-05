package com.socialmedia.app.dto;

/**
 * Represents a sign-in request containing the username and password.
 */
public record SignInRequest(String username, String password) {
}
