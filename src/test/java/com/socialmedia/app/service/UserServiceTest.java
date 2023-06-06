package com.socialmedia.app.service;

import com.socialmedia.app.dto.UserDto;
import com.socialmedia.app.model.User;
import com.socialmedia.app.repository.FriendStatusRepository;
import com.socialmedia.app.repository.UserRepository;
import com.socialmedia.app.util.UserConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendStatusRepository friendStatusRepository;

    @Mock
    private UserConvertor userConvertor;

    @Mock
    private Principal principal;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        reset(userRepository, friendStatusRepository, userConvertor, principal);
    }

    @Test
    void testGetUserByUsername_ExistingUser_ReturnsUserDto() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        UserDto expectedDto = new UserDto();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userConvertor.convertToDto(user)).thenReturn(expectedDto);

        // Act
        UserDto actualDto = userService.getUserByUsername(username);

        // Assert
        assertEquals(expectedDto, actualDto);
    }

    @Test
    void testGetUserByUsername_NonExistingUser_ThrowsException() {
        // Arrange
        String username = "nonexistinguser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> userService.getUserByUsername(username));
    }

    @Test
    void testFollowUser_ValidUser_SuccessfullyFollows() {
        // Arrange
        String followerUsername = "follower";
        String followingUsername = "following";
        User follower = new User();
        follower.setUsername(followerUsername);
        follower.setFollowing(new HashSet<>());
        User following = new User();
        following.setUsername(followingUsername);
        following.setFollowing(new HashSet<>());
        when(userRepository.findByUsername(followingUsername)).thenReturn(Optional.of(following));
        when(userRepository.findByUsername(null)).thenReturn(Optional.empty());
        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(follower));

        // Act
        assertDoesNotThrow(() -> userService.followUser(followingUsername, principal));

        // Assert
        assertTrue(follower.getFollowing().contains(following));
        verify(userRepository, times(1)).save(follower);
    }
}