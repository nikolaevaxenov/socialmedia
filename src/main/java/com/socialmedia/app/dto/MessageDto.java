package com.socialmedia.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) class representing a message.
 */
@Getter
@Setter
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private String text;
    private String fromUser;
    private Long chat;
    private Instant createdAt;

    /**
     * Constructs a new MessageDto object with the specified text, sender, and chat ID.
     *
     * @param text      the text content of the message
     * @param fromUser  the username of the sender
     * @param chat      the ID of the chat the message belongs to
     */
    public MessageDto(String text, String fromUser, Long chat) {
        this.text = text;
        this.fromUser = fromUser;
        this.chat = chat;
    }

    /**
     * Returns a string representation of the MessageDto object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "MessageDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", fromUser='" + fromUser + '\'' +
                ", chat=" + chat +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Checks whether this MessageDto object is equal to another object.
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageDto that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getText(), that.getText()) && Objects.equals(getFromUser(), that.getFromUser()) && Objects.equals(getChat(), that.getChat()) && Objects.equals(getCreatedAt(), that.getCreatedAt());
    }

    /**
     * Generates a hash code value for this MessageDto object.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), getFromUser(), getChat(), getCreatedAt());
    }
}
