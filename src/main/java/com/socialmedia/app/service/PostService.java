package com.socialmedia.app.service;

import com.socialmedia.app.dto.PostDto;
import com.socialmedia.app.model.Image;
import com.socialmedia.app.model.Post;
import com.socialmedia.app.repository.ImageRepository;
import com.socialmedia.app.repository.PostRepository;
import com.socialmedia.app.repository.UserRepository;
import com.socialmedia.app.util.PostConvertor;
import org.apache.tika.Tika;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing post functionality.
 */
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PostConvertor postConvertor;

    private final Tika tika = new Tika();

    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository, PostConvertor postConvertor) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.postConvertor = postConvertor;
    }

    /**
     * Retrieves a post by its ID.
     *
     * @param id the ID of the post
     * @return the post DTO
     * @throws ResponseStatusException if the post is not found
     */
    public PostDto getPostById(Long id) {
        return postRepository
                .findById(id)
                .map(postConvertor::convertToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with given id not found!"));
    }

    /**
     * Retrieves all posts by a given username.
     *
     * @param username the username of the user
     * @return a set of post DTOs
     * @throws ResponseStatusException if the user is not found
     */
    public Set<PostDto> getPostsByUsername(String username) {
        return postRepository
                .findAllByUser_UsernameOrderByCreatedAtDesc(username)
                .map(post -> post.stream().map(postConvertor::convertToDto).collect(Collectors.toSet()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));
    }

    /**
     * Creates a new post.
     *
     * @param post      the post object
     * @param principal the authenticated user principal
     * @return the created post DTO
     * @throws ResponseStatusException if the user is not found
     */
    public PostDto createPost(Post post, Principal principal) {
        var user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));
        post.setUser(user);
        postRepository.save(post);

        return postConvertor.convertToDto(post);
    }

    /**
     * Edits a post.
     *
     * @param editedPost the edited post object
     * @param principal  the authenticated user principal
     * @return the updated post DTO
     * @throws ResponseStatusException if the post is not found or the user is not authorized to edit the post
     */
    public PostDto editPost(Post editedPost, Principal principal) {
        var post = postRepository
                .findById(editedPost.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with given id not found!"));
        if (Objects.equals(post.getUser().getUsername(), principal.getName())) {
            if (editedPost.getTitle() != null)
                post.setTitle(editedPost.getTitle());
            if (editedPost.getBody() != null)
                post.setBody(editedPost.getBody());

            postRepository.save(post);

            return postConvertor.convertToDto(post);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't edit someone's post!");
        }
    }

    /**
     * Deletes a post.
     *
     * @param id        the ID of the post
     * @param principal the authenticated user principal
     * @throws ResponseStatusException if the post is not found or the user is not authorized to delete the post
     */
    public void deletePost(Long id, Principal principal) {
        var post = postRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (Objects.equals(post.getUser().getUsername(), principal.getName()))
            postRepository.delete(post);
        else
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete someone's post!");
    }

    /**
     * Retrieves an image by its ID.
     *
     * @param id the ID of the image
     * @return the image as a ResponseEntity with InputStreamResource
     * @throws IOException               if there is an error while reading the image file
     * @throws ResponseStatusException   if the image is not found
     */
    public ResponseEntity<InputStreamResource> getImage(Long id) throws IOException {
        var image = imageRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image with given id not found!"));

        MediaType contentType = MediaType.valueOf(image.getMediaType());
        InputStream in = Files.newInputStream(Paths.get("savedImages/" + image.getLocation()));

        return ResponseEntity
                .ok()
                .contentType(contentType)
                .body(new InputStreamResource(in));
    }

    /**
     * Adds images to a post.
     *
     * @param id     the ID of the post
     * @param images an array of MultipartFile objects representing the images
     * @throws IOException             if there is an error while reading or saving the images
     * @throws ResponseStatusException if the post is not found, the image is empty, the uploaded file is not an image,
     *                                 or the uploaded image is too large
     */
    public void addImageToPost(Long id, MultipartFile[] images) throws IOException {
        var post = postRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with given id not found!"));

        String[] allowedTypes = {"image/jpeg", "image/png", "image/webp"};

        for (var image : images) {
            String fileType = tika.detect(image.getBytes());

            if (image.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Image is empty!");
            } else if (!Arrays.asList(allowedTypes).contains(fileType)) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Uploaded file is not an image! Supported formats: JPEG, PNG, WEBP.");
            } else if (image.getSize() > 20971520L) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Uploaded image is too large! Maximum size is 20MB");
            } else {
                String newFilename = UUID.randomUUID() + "." + fileType.split("/")[1];

                Path path = Paths.get("savedImages");
                Files.copy(image.getInputStream(), path.resolve(newFilename));

                var savedImage = new Image(newFilename, fileType);
                savedImage.setPost(post);
                imageRepository.save(savedImage);
            }
        }
    }

    /**
     * Retrieves the user feed, which includes posts from the users that the authenticated user is following.
     *
     * @param principal the authenticated user principal
     * @param pageable  the pageable object for pagination
     * @return a list of post DTOs
     * @throws ResponseStatusException if the user is not found
     */
    public List<PostDto> getUserFeed(Principal principal, PageRequest pageable) {
        var user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));

        var posts = postRepository
                .findAllByUserIn(user.getFollowing(), pageable);

        return posts
                .stream()
                .map(postConvertor::convertToDto)
                .toList();
    }
}
