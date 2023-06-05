package com.socialmedia.app.controller;

import com.socialmedia.app.dto.PostDto;
import com.socialmedia.app.model.Post;
import com.socialmedia.app.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Posts", description = "Endpoints for handling post-related operations")
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
    @Operation(summary = "Get Post by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public PostDto getPostById(@Parameter(description = "The ID of the post to retrieve.", required = true) @PathVariable Long id) {
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
    @Operation(summary = "Get Posts by Username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PostDto.class)))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Set<PostDto> getPostsByUsername(@Parameter(description = "The username of the user to retrieve the posts for.", required = true) @PathVariable String username) {
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
    @Operation(summary = "Create Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully",
                    content = @Content(schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
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
    @Operation(summary = "Edit Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Post edited successfully",
                    content = @Content(schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "403", description = "You can't edit someone's post!"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
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
    @Operation(summary = "Delete Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully",
                    content = @Content(schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "403", description = "You can't delete someone's post!"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public void deletePost(@Parameter(description = "The ID of the post to be deleted.", required = true) @PathVariable Long id,
                           Principal principal) {
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
    @Operation(summary = "Get Post Image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post image retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Image with given id not found")
    })
    public ResponseEntity<InputStreamResource> getImage(@Parameter(description = "The ID of the post to retrieve the image from.", required = true) @PathVariable Long id) throws IOException {
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
    @Operation(summary = "Add Image to Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image added to post successfully"),
            @ApiResponse(responseCode = "406", description = "Invalid image format"),
            @ApiResponse(responseCode = "406", description = "Uploaded image is too large"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public void addImageToPost(@Parameter(description = "The ID of the post to add the image to.", required = true) @PathVariable Long id,
                               @Parameter(description = "The image file(s) to be added to the post.", required = true) @RequestParam MultipartFile[] image) throws IOException {
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
    @Operation(summary = "Get User Feed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User feed retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public List<PostDto> getUserFeed(@Parameter(description = "The page number of the feed to retrieve. Default is 0.", example = "0") @RequestParam(defaultValue = "0") int page,
                                     @Parameter(description = "The size of each page of the feed. Default is 3.", example = "3") @RequestParam(defaultValue = "3") int size,
                                     @Parameter(description = "The sorting direction of the feed. Default is DESC.", example = "DESC") @RequestParam(defaultValue = "DESC") String direction,
                                     Principal principal) {

        if (!Objects.equals(direction, "ASC") && !Objects.equals(direction, "DESC")) {
            direction = "DESC";
        }

        return postService.getUserFeed(principal, PageRequest.of(page, size, Sort.Direction.fromString(direction), "createdAt"));
    }
}
