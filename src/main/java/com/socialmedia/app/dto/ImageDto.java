package com.socialmedia.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ImageDto {
    private Long id;
    private String location;
    private String mediaType;
    private Long post;

    public ImageDto(String location, String mediaType) {
        this.location = location;
        this.mediaType = mediaType;
    }

    @Override
    public String toString() {
        return "ImageDto{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", post=" + post +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageDto imageDto)) return false;
        return Objects.equals(getId(), imageDto.getId()) && Objects.equals(getLocation(), imageDto.getLocation()) && Objects.equals(getMediaType(), imageDto.getMediaType()) && Objects.equals(getPost(), imageDto.getPost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLocation(), getMediaType(), getPost());
    }
}
