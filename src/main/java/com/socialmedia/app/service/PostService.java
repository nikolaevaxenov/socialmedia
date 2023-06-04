package com.socialmedia.app.service;

import com.socialmedia.app.model.Image;
import com.socialmedia.app.model.Post;
import com.socialmedia.app.repository.ImageRepository;
import com.socialmedia.app.repository.PostRepository;
import com.socialmedia.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.core.io.InputStreamResource;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    private final Tika tika = new Tika();

    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public Post getPostById(Long id) {
        return postRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with given id not found!"));
    }

    public Set<Post> getPostsByUsername(String username) {
        return postRepository
                .findAllByUser_UsernameOrderByCreatedAtDesc(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));
    }

    public Post createPost(Post post, Principal principal) {
        var user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));
        post.setUser(user);

        return postRepository.save(post);
    }

    public Post editPost(Post editedPost, Principal principal) {
        var post = postRepository
                .findById(editedPost.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with given id not found!"));
        if (Objects.equals(post.getUser().getUsername(), principal.getName())) {
            if (editedPost.getTitle() != null)
                post.setTitle(editedPost.getTitle());
            if (editedPost.getBody() != null)
                post.setBody(editedPost.getBody());

            return postRepository.save(post);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't edit someone's post!");
        }
    }

    public void deletePost(Long id, Principal principal) {
        var post = postRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (Objects.equals(post.getUser().getUsername(), principal.getName()))
            postRepository.delete(post);
        else
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete someone's post!");
    }

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
}
