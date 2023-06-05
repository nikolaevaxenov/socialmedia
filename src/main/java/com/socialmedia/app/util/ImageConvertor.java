package com.socialmedia.app.util;

import com.socialmedia.app.dto.ImageDto;
import com.socialmedia.app.model.Image;
import com.socialmedia.app.model.Post;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ImageConvertor {
    private final ModelMapper modelMapper;

    public ImageConvertor() {
        this.modelMapper = new ModelMapper();

        Converter<Post, Long> postToPostId = ctx -> ctx.getSource().getId();

        modelMapper.createTypeMap(Image.class, ImageDto.class)
                .addMappings(map -> map
                        .using(postToPostId)
                        .map(Image::getPost, ImageDto::setPost));
    }

    public ImageDto convertToDto(Image image) {
        return modelMapper.map(image, ImageDto.class);
    }
}
