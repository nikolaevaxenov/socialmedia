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
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping("/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Set<PostDto> getPostsByUsername(@PathVariable String username) {
        return postService.getPostsByUsername(username);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@RequestBody Post post, Principal principal) {
        return postService.createPost(post, principal);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PostDto editPost(@RequestBody Post post, Principal principal) {
        return postService.editPost(post, principal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable Long id, Principal principal) {
        postService.deletePost(id, principal);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable Long id) throws IOException {
        return postService.getImage(id);
    }

    @PostMapping("/image/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void addImageToPost(@PathVariable Long id, @RequestParam MultipartFile[] image) throws IOException {
        postService.addImageToPost(id, image);
    }

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
