package com.socialmedia.app.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Represents an image.
 */
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
    private Post post;

    /**
     * Constructs a new Image object with the given location and media type.
     *
     * @param location   The location of the image.
     * @param mediaType  The media type of the image.
     */
    public Image(String location, String mediaType) {
        this.location = location;
        this.mediaType = mediaType;
    }

    /**
     * Returns the string representation of the Image object.
     *
     * @return The string representation of the Image object.
     */
    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }

    /**
     * Checks if the Image object is equal to another object.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image image)) return false;
        return Objects.equals(getId(), image.getId()) && Objects.equals(getLocation(), image.getLocation()) && Objects.equals(getMediaType(), image.getMediaType());
    }

    /**
     * Returns the hash code value for the Image object.
     *
     * @return The hash code value for the Image object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLocation(), getMediaType());
    }
}
