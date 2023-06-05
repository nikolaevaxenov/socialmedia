package com.socialmedia.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) class representing an image.
 */
@Getter
@Setter
@NoArgsConstructor
public class ImageDto {
    private Long id;
    private String location;
    private String mediaType;
    private Long post;

    /**
     * Constructs a new ImageDto object with the specified location and media type.
     *
     * @param location   the location of the image
     * @param mediaType  the media type of the image
     */
    public ImageDto(String location, String mediaType) {
        this.location = location;
        this.mediaType = mediaType;
    }

    /**
     * Returns a string representation of the ImageDto object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "ImageDto{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", post=" + post +
                '}';
    }

    /**
     * Checks whether this ImageDto object is equal to another object.
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageDto imageDto)) return false;
        return Objects.equals(getId(), imageDto.getId()) && Objects.equals(getLocation(), imageDto.getLocation()) && Objects.equals(getMediaType(), imageDto.getMediaType()) && Objects.equals(getPost(), imageDto.getPost());
    }

    /**
     * Generates a hash code value for this ImageDto object.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLocation(), getMediaType(), getPost());
    }
}
