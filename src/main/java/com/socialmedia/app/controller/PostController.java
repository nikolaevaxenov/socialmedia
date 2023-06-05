package com.socialmedia.app.controller;

import com.socialmedia.app.dto.PostDto;
import com.socialmedia.app.model.Post;
import com.socialmedia.app.service.PostService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Controller class for handling post-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    /**
     * Constructs the PostController class.
     *
     * @param postService the PostService implementation.
     */
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Retrieves a post by its ID.
     *
     * @param id the ID of the post to retrieve.
     * @return the PostDto object representing the post.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    /**
     * Retrieves all posts by the specified username.
     *
     * @param username the username of the user to retrieve the posts for.
     * @return a set of PostDto objects representing the posts.
     */
    @GetMapping("/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Set<PostDto> getPostsByUsername(@PathVariable String username) {
        return postService.getPostsByUsername(username);
    }

    /**
     * Creates a new post.
     *
     * @param post      the Post object containing the post details.
     * @param principal the authenticated principal user.
     * @return the created PostDto object representing the new post.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@RequestBody Post post, Principal principal) {
        return postService.createPost(post, principal);
    }

    /**
     * Edits an existing post.
     *
     * @param post      the updated Post object containing the post details.
     * @param principal the authenticated principal user.
     * @return the edited PostDto object representing the updated post.
     */
    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PostDto editPost(@RequestBody Post post, Principal principal) {
        return postService.editPost(post, principal);
    }

    /**
     * Deletes a post by its ID.
     *
     * @param id        the ID of the post to delete.
     * @param principal the authenticated principal user.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable Long id, Principal principal) {
        postService.deletePost(id, principal);
    }

    /**
     * Retrieves the image associated with a post.
     *
     * @param id the ID of the post to retrieve the image for.
     * @return the ResponseEntity containing the image as an InputStreamResource.
     * @throws IOException if an error occurs while retrieving the image.
     */
    @GetMapping("/image/{id}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable Long id) throws IOException {
        return postService.getImage(id);
    }

    /**
     * Adds an image to a post.
     *
     * @param id    the ID of the post to add the image to.
     * @param image the image file to be added.
     * @throws IOException if an error occurs while adding the image to the post.
     */
    @PostMapping("/image/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void addImageToPost(@PathVariable Long id, @RequestParam MultipartFile[] image) throws IOException {
        postService.addImageToPost(id, image);
    }

    /**
     * Retrieves the user feed.
     *
     * @param page      the page number of the feed.
     * @param size      the number of posts per page.
     * @param direction the sorting direction of the posts (ASC or DESC).
     * @param principal the authenticated principal user.
     * @return a list of PostDto objects representing the user feed.
     */
    @GetMapping("/feed")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getUserFeed(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "3") int size,
                                     @RequestParam(defaultValue = "DESC") String direction,
                                     Principal principal) {

        if (!Objects.equals(direction, "ASC") && !Objects.equals(direction, "DESC")) {
            direction = "DESC";
        }

        return postService.getUserFeed(principal, PageRequest.of(page, size, Sort.Direction.fromString(direction), "createdAt"));
    }
}
