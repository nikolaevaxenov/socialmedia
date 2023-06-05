package com.socialmedia.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

/**
 * Represents a user.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max = 32)
    @Column(unique = true)
    private String username;

    @Email
    @Size(max = 256)
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Post> posts;

    @ManyToMany
    private Set<User> following;

    @ManyToMany(mappedBy = "following")
    private Set<User> followers;

    @ManyToMany
    private Set<User> friends;

    /**
     * Constructs a new User object with the given username, email, and password.
     *
     * @param username The username of the user.
     * @param email    The email address of the user.
     * @param password The password of the user.
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Adds a user to the set of users that the user is following.
     *
     * @param user The user to follow.
     */
    public void followUser(User user) {
        this.following.add(user);
    }

    /**
     * Removes a user from the set of users that the user is following.
     *
     * @param user The user to unfollow.
     */
    public void unFollowUser(User user) {
        this.following.remove(user);
    }

    /**
     * Adds a user to the set of friends of the user.
     *
     * @param user The user to add as a friend.
     */
    public void addFriend(User user) {
        this.friends.add(user);
    }

    /**
     * Removes a user from the set of friends of the user.
     *
     * @param user The user to remove from friends.
     */
    public void removeFriend(User user) {
        this.friends.remove(user);
    }

    /**
     * Checks if a user is a friend of the user.
     *
     * @param user The user to search for.
     * @return true if the user is a friend, false otherwise.
     */
    public boolean searchFriend(User user) {
        return this.friends.contains(user);
    }

    /**
     * Returns the string representation of the User object.
     *
     * @return The string representation of the User object.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    /**
     * Checks if the User object is equal to another object.
     *
     * @param o The object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getPassword(), user.getPassword());
    }

    /**
     * Returns the hash code of the User object.
     *
     * @return The hash code of the User object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getEmail());
    }
}
