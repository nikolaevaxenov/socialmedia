package com.socialmedia.app.service;

import com.socialmedia.app.dto.PostDto;
import com.socialmedia.app.model.Post;
import com.socialmedia.app.model.User;
import com.socialmedia.app.repository.ImageRepository;
import com.socialmedia.app.repository.PostRepository;
import com.socialmedia.app.repository.UserRepository;
import com.socialmedia.app.util.PostConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private PostConvertor postConvertor;

    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postService = new PostService(postRepository, userRepository, imageRepository, postConvertor);
    }

    @Test
    void getPostById_PostExists_ReturnsPostDto() {
        // Arrange
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        PostDto postDto = new PostDto();
        postDto.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postConvertor.convertToDto(post)).thenReturn(postDto);

        // Act
        PostDto result = postService.getPostById(postId);

        // Assert
        assertNotNull(result);
        assertEquals(postId, result.getId());
        verify(postRepository, times(1)).findById(postId);
        verifyNoMoreInteractions(postRepository);
        verify(postConvertor, times(1)).convertToDto(post);
        verifyNoMoreInteractions(postConvertor);
    }

    @Test
    void getPostById_PostDoesNotExist_ThrowsException() {
        // Arrange
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> postService.getPostById(postId));
        verify(postRepository, times(1)).findById(postId);
        verifyNoMoreInteractions(postRepository);
        verifyNoInteractions(postConvertor);
    }

    @Test
    void getPostsByUsername_UserExists_ReturnsPostDtoSet() {
        // Arrange
        String username = "testUser";
        Post post1 = new Post();
        Post post2 = new Post();
        Set<Post> posts = new HashSet<>(Arrays.asList(post1, post2));
        PostDto postDto1 = new PostDto();
        PostDto postDto2 = new PostDto();
        Set<PostDto> postDtoSet = new HashSet<>(Arrays.asList(postDto1, postDto2));

        when(postRepository.findAllByUser_UsernameOrderByCreatedAtDesc(username)).thenReturn(Optional.of(posts));
        when(postConvertor.convertToDto(post1)).thenReturn(postDto1);
        when(postConvertor.convertToDto(post2)).thenReturn(postDto2);

        // Act
        Set<PostDto> result = postService.getPostsByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(postDto1));
        assertTrue(result.contains(postDto2));
        verify(postRepository, times(1)).findAllByUser_UsernameOrderByCreatedAtDesc(username);
        verifyNoMoreInteractions(postRepository);
        verify(postConvertor, times(1)).convertToDto(post1);
        verify(postConvertor, times(1)).convertToDto(post2);
        verifyNoMoreInteractions(postConvertor);
    }

    @Test
    void getPostsByUsername_UserDoesNotExist_ThrowsException() {
        // Arrange
        String username = "testUser";

        when(postRepository.findAllByUser_UsernameOrderByCreatedAtDesc(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> postService.getPostsByUsername(username));
        verify(postRepository, times(1)).findAllByUser_UsernameOrderByCreatedAtDesc(username);
        verifyNoMoreInteractions(postRepository);
        verifyNoInteractions(postConvertor);
    }

    @Test
    void createPost_ValidPost_ReturnsCreatedPostDto() {
        // Arrange
        Post post = new Post();
        post.setTitle("Test Title");
        post.setBody("Test Body");
        Principal principal = mock(Principal.class);
        User user = new User();
        user.setUsername("testUser");

        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(user));
        when(postRepository.save(post)).thenReturn(post);
        when(postConvertor.convertToDto(post)).thenReturn(new PostDto());

        // Act
        PostDto result = postService.createPost(post, principal);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findByUsername(principal.getName());
        verifyNoMoreInteractions(userRepository);
        verify(postRepository, times(1)).save(post);
        verifyNoMoreInteractions(postRepository);
        verify(postConvertor, times(1)).convertToDto(post);
        verifyNoMoreInteractions(postConvertor);
    }

    @Test
    void createPost_UserNotFound_ThrowsException() {
        // Arrange
        Post post = new Post();
        Principal principal = mock(Principal.class);

        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> postService.createPost(post, principal));
        verify(userRepository, times(1)).findByUsername(principal.getName());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(postRepository);
        verifyNoInteractions(postConvertor);
    }

    @Test
    void editPost_PostExistsAndUserAuthorized_ReturnsUpdatedPostDto() {
        // Arrange
        Post editedPost = new Post();
        editedPost.setId(1L);
        editedPost.setTitle("Updated Title");
        editedPost.setBody("Updated Body");
        Principal principal = mock(Principal.class);
        User user = new User();
        user.setUsername("testUser");
        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setTitle("Old Title");
        existingPost.setBody("Old Body");
        existingPost.setUser(user);

        when(postRepository.findById(editedPost.getId())).thenReturn(Optional.of(existingPost));
        when(postRepository.save(existingPost)).thenReturn(existingPost);
        when(postConvertor.convertToDto(existingPost)).thenReturn(new PostDto());
        when(principal.getName()).thenReturn("testUser");

        // Act
        PostDto result = postService.editPost(editedPost, principal);

        // Assert
        assertNotNull(result);
        assertEquals(editedPost.getTitle(), existingPost.getTitle());
        assertEquals(editedPost.getBody(), existingPost.getBody());
        verify(postRepository, times(1)).findById(editedPost.getId());
        verify(postRepository, times(1)).save(existingPost);
        verifyNoMoreInteractions(postRepository);
        verify(postConvertor, times(1)).convertToDto(existingPost);
        verifyNoMoreInteractions(postConvertor);
    }

    @Test
    void editPost_PostNotFound_ThrowsException() {
        // Arrange
        Post editedPost = new Post();
        editedPost.setId(1L);
        Principal principal = mock(Principal.class);

        when(postRepository.findById(editedPost.getId())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> postService.editPost(editedPost, principal));
        verify(postRepository, times(1)).findById(editedPost.getId());
        verifyNoMoreInteractions(postRepository);
        verifyNoInteractions(postConvertor);
    }

    @Test
    void deletePost_PostExistsAndUserAuthorized_DeletesPost() {
        // Arrange
        Long postId = 1L;
        Principal principal = mock(Principal.class);
        User user = new User();
        user.setUsername("testUser");
        Post existingPost = new Post();
        existingPost.setId(postId);
        existingPost.setUser(user);

        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));
        when(principal.getName()).thenReturn("testUser");

        // Act
        postService.deletePost(postId, principal);

        // Assert
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).delete(existingPost);
        verifyNoMoreInteractions(postRepository);
        verifyNoInteractions(postConvertor);
    }

    @Test
    void deletePost_PostNotFound_ThrowsException() {
        // Arrange
        Long postId = 1L;
        Principal principal = mock(Principal.class);

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> postService.deletePost(postId, principal));
        verify(postRepository, times(1)).findById(postId);
        verifyNoMoreInteractions(postRepository);
        verifyNoInteractions(postConvertor);
    }
}