package com.socialmedia.app.util;

import com.socialmedia.app.dto.PostDto;
import com.socialmedia.app.model.Image;
import com.socialmedia.app.model.Post;
import com.socialmedia.app.model.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PostConvertor {
    private final ModelMapper modelMapper;

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

    public PostDto convertToDto(Post post) {
        return modelMapper.map(post, PostDto.class);
    }
}
