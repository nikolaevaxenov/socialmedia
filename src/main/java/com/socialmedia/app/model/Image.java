package com.socialmedia.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "images")
public class Image {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    private Long id;

    private String location;

    private String mediaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonBackReference(value = "images")
    private Post post;

    public Image(String location, String mediaType) {
        this.location = location;
        this.mediaType = mediaType;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image image)) return false;
        return Objects.equals(getId(), image.getId()) && Objects.equals(getLocation(), image.getLocation()) && Objects.equals(getMediaType(), image.getMediaType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLocation(), getMediaType());
    }
}
