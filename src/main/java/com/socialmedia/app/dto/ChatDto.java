package com.socialmedia.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

/**
 * Data Transfer Object (DTO) class representing a chat.
 */
@Getter
@Setter
@NoArgsConstructor
public class ChatDto {
    private Long id;
    private String user1;
    private String user2;
    private Set<MessageDto> message;

    /**
     * Constructs a new ChatDto object with the specified ID, user1, and user2.
     *
     * @param id    the ID of the chat
     * @param user1 the username of the first user in the chat
     * @param user2 the username of the second user in the chat
     */
    public ChatDto(Long id, String user1, String user2) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
    }

    /**
     * Checks whether this ChatDto object is equal to another object.
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatDto chatDto)) return false;
        return Objects.equals(getId(), chatDto.getId()) && Objects.equals(getUser1(), chatDto.getUser1()) && Objects.equals(getUser2(), chatDto.getUser2());
    }

    /**
     * Generates a hash code value for this ChatDto object.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser1(), getUser2());
    }
}
