package com.socialmedia.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a message in a chat.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chat chat;

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    private Instant createdAt;

    /**
     * Constructs a new Message object with the given text, user, and chat.
     *
     * @param text     The text content of the message.
     * @param fromUser The user who sent the message.
     * @param chat     The chat to which the message belongs.
     */
    public Message(String text, User fromUser, Chat chat) {
        this.text = text;
        this.fromUser = fromUser;
        this.chat = chat;
    }

    /**
     * Returns the string representation of the Message object.
     *
     * @return The string representation of the Message object.
     */
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Checks if the Message object is equal to another object.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message message)) return false;
        return Objects.equals(getId(), message.getId()) && Objects.equals(getText(), message.getText()) && Objects.equals(getCreatedAt(), message.getCreatedAt());
    }

    /**
     * Returns the hash code value for the Message object.
     *
     * @return The hash code value for the Message object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), getCreatedAt());
    }
}
