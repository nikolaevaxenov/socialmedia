package com.socialmedia.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

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

    public PostDto(String title, String body) {
        this.title = title;
        this.body = body;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostDto postDto)) return false;
        return Objects.equals(getId(), postDto.getId()) && Objects.equals(getTitle(), postDto.getTitle()) && Objects.equals(getBody(), postDto.getBody()) && Objects.equals(getCreatedAt(), postDto.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getBody(), getCreatedAt());
    }
}
