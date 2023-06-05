package com.socialmedia.app.util;

import com.socialmedia.app.dto.PostDto;
import com.socialmedia.app.model.Image;
import com.socialmedia.app.model.Post;
import com.socialmedia.app.model.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Utility class for converting Post entities to PostDto objects.
 */
@Component
public class PostConvertor {
    private final ModelMapper modelMapper;

    /**
     * Constructs a PostConvertor and configures the ModelMapper.
     */
    public PostConvertor() {
        this.modelMapper = new ModelMapper();

        Converter<User, String> userToUsernameString = ctx -> ctx.getSource().getUsername();

        Converter<Set<Image>, long[]> imagesSetToLongArray =
                ctx -> ctx.getSource()
                        .stream()
                        .mapToLong(Image::getId)
                        .toArray();

        modelMapper.createTypeMap(Post.class, PostDto.class)
                .addMappings(map -> map
                        .using(userToUsernameString)
                        .map(Post::getUser, PostDto::setUser)
                )
                .addMappings(map -> map
                        .using(imagesSetToLongArray)
                        .map(Post::getImages, PostDto::setImages));
    }

    /**
     * Converts a Post entity to a PostDto object.
     *
     * @param post the Post entity to convert
     * @return the PostDto object
     */
    public PostDto convertToDto(Post post) {
        return modelMapper.map(post, PostDto.class);
    }
}
