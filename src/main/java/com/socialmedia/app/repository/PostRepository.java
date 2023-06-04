package com.socialmedia.app.repository;

import com.socialmedia.app.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    Set<Post> findAllByOrderByCreatedAtDesc();
    Optional<Set<Post>> findAllByUser_UsernameOrderByCreatedAtDesc(String username);
}
