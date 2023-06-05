package com.socialmedia.app.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

/**
 * Represents a chat between two users.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chats")
public class Chat {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user2;

    @OneToMany(mappedBy = "chat")
    private Set<Message> message;

    /**
     * Constructs a new Chat object with the given users.
     *
     * @param user1 The first user participating in the chat.
     * @param user2 The second user participating in the chat.
     */
    public Chat(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    /**
     * Returns the string representation of the Chat object.
     *
     * @return The string representation of the Chat object.
     */
    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                '}';
    }

    /**
     * Checks if the Chat object is equal to another object.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chat chat)) return false;
        return Objects.equals(getId(), chat.getId());
    }

    /**
     * Returns the hash code value for the Chat object.
     *
     * @return The hash code value for the Chat object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
