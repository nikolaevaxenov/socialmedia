package com.socialmedia.app.util;

import com.socialmedia.app.dto.UserDto;
import com.socialmedia.app.model.Post;
import com.socialmedia.app.model.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserConvertor {
    private final ModelMapper modelMapper;

    public UserConvertor() {
        this.modelMapper = new ModelMapper();

        Converter<Set<Post>, long[]> postsSetToLongArray =
                ctx -> ctx.getSource()
                        .stream()
                        .mapToLong(Post::getId)
                        .toArray();

        Converter<Set<User>, Set<String>> usersSetToUsernameSet =
                ctx -> ctx.getSource()
                        .stream()
                        .map(User::getUsername)
                        .collect(Collectors.toSet());

        modelMapper.createTypeMap(User.class, UserDto.class)
                .addMappings(map -> map
                        .using(postsSetToLongArray)
                        .map(
                                User::getPosts,
                                UserDto::setPosts
                        )
                )
                .addMappings(map -> map
                        .using(usersSetToUsernameSet)
                        .map(
                                User::getFollowers,
                                UserDto::setFollowers
                        )
                )
                .addMappings(map -> map
                        .using(usersSetToUsernameSet)
                        .map(
                                User::getFollowing,
                                UserDto::setFollowing
                        )
                )
                .addMappings(map -> map
                        .using(usersSetToUsernameSet)
                        .map(
                                User::getFriends,
                                UserDto::setFriends
                        )
                );
    }

    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
