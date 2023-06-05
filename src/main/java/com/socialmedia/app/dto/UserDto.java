package com.socialmedia.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

/**
 * Data Transfer Object (DTO) class representing a user.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private long[] posts;
    private Set<String> following;
    private Set<String> followers;
    private Set<String> friends;

    /**
     * Constructs a UserDto object with the specified username and email.
     *
     * @param username The username of the user.
     * @param email    The email of the user.
     */
    public UserDto(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * Returns a string representation of the UserDto object.
     *
     * @return A string representation of the UserDto object.
     */
    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    /**
     * Compares this UserDto object to the specified object. The result is true if and only if
     * the argument is not null and is a UserDto object that represents the same username and email.
     *
     * @param o The object to compare this UserDto against.
     * @return true if the given object represents a UserDto equivalent to this UserDto, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto userDto)) return false;
        return Objects.equals(getUsername(), userDto.getUsername()) && Objects.equals(getEmail(), userDto.getEmail());
    }

    /**
     * Returns the hash code value for the UserDto object.
     *
     * @return The hash code value for the UserDto object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getEmail());
    }
}
