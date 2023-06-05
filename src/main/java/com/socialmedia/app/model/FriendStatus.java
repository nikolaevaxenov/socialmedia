package com.socialmedia.app.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Represents the friend request status between two users.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "friend_status")
public class FriendStatus {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User userFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    private User userTo;

    /**
     * Constructs a new FriendStatus object with the given users.
     *
     * @param userFrom The user who sent the friend request.
     * @param userTo   The user who received the friend request.
     */
    public FriendStatus(User userFrom, User userTo) {
        this.userFrom = userFrom;
        this.userTo = userTo;
    }

    /**
     * Returns the string representation of the FriendStatus object.
     *
     * @return The string representation of the FriendStatus object.
     */
    @Override
    public String toString() {
        return "FriendStatus{" +
                "id=" + id +
                '}';
    }

    /**
     * Checks if the FriendStatus object is equal to another object.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendStatus that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    /**
     * Returns the hash code value for the FriendStatus object.
     *
     * @return The hash code value for the FriendStatus object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
