package com.socialmedia.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) class representing a post.
 */
@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String body;
    private Instant createdAt;
    private String user;
    private long[] images;

    /**
     * Constructs a new PostDto object with the specified title and body.
     *
     * @param title the title of the post
     * @param body  the body content of the post
     */
    public PostDto(String title, String body) {
        this.title = title;
        this.body = body;
    }

    /**
     * Returns a string representation of the PostDto object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Checks whether this PostDto object is equal to another object.
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostDto postDto)) return false;
        return Objects.equals(getId(), postDto.getId()) && Objects.equals(getTitle(), postDto.getTitle()) && Objects.equals(getBody(), postDto.getBody()) && Objects.equals(getCreatedAt(), postDto.getCreatedAt());
    }

    /**
     * Generates a hash code value for this PostDto object.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getBody(), getCreatedAt());
    }
}
