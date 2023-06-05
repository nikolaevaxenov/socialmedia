package com.socialmedia.app.util;

import com.socialmedia.app.dto.ImageDto;
import com.socialmedia.app.model.Image;
import com.socialmedia.app.model.Post;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Utility class for converting Image entities to ImageDto objects.
 */
@Component
public class ImageConvertor {
    private final ModelMapper modelMapper;

    /**
     * Constructs an ImageConvertor and configures the ModelMapper.
     */
    public ImageConvertor() {
        this.modelMapper = new ModelMapper();

        Converter<Post, Long> postToPostId = ctx -> ctx.getSource().getId();

        modelMapper.createTypeMap(Image.class, ImageDto.class)
                .addMappings(map -> map
                        .using(postToPostId)
                        .map(Image::getPost, ImageDto::setPost));
    }

    /**
     * Converts an Image entity to an ImageDto object.
     *
     * @param image the Image entity to convert
     * @return the ImageDto object
     */
    public ImageDto convertToDto(Image image) {
        return modelMapper.map(image, ImageDto.class);
    }
}
