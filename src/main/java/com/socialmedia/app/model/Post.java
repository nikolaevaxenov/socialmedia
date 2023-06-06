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
import java.util.Set;

/**
 * Represents a post.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String title;

    private String body;

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "post", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Image> images;

    /**
     * Constructs a new Post object with the given title and body.
     *
     * @param title The title of the post.
     * @param body  The body content of the post.
     */
    public Post(String title, String body) {
        this.title = title;
        this.body = body;
    }

    /**
     * Returns the string representation of the Post object.
     *
     * @return The string representation of the Post object.
     */
    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Checks if the Post object is equal to another object.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(getId(), post.getId()) && Objects.equals(getTitle(), post.getTitle()) && Objects.equals(getBody(), post.getBody()) && Objects.equals(getCreatedAt(), post.getCreatedAt());
    }

    /**
     * Returns the hash code value for the Post object.
     *
     * @return The hash code value for the Post object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getBody(), getCreatedAt());
    }
}
