package com.socialmedia.app.repository;

import com.socialmedia.app.model.Post;
import com.socialmedia.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Set<Post>> findAllByUser_UsernameOrderByCreatedAtDesc(String username);
    //Optional<Set<Post>> findAllByUser_FollowingInOrderByCreatedAtDesc(Collection<Set<User>> user_following);
    Page<Post> findAllByUserIn(Set<User> users, Pageable pageable);
}
