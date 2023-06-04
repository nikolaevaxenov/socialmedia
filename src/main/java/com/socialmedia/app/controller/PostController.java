package com.socialmedia.app.controller;

import com.socialmedia.app.model.Post;
import com.socialmedia.app.service.PostService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
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
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping("/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Post> getPostsByUsername(@PathVariable String username) {
        return postService.getPostsByUsername(username);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody Post post, Principal principal) {
        return postService.createPost(post, principal);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Post editPost(@RequestBody Post post, Principal principal) {
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
}
