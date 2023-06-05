package com.socialmedia.app.repository;

import com.socialmedia.app.model.Post;
import com.socialmedia.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for Post entities.
 * It provides methods for CRUD operations and querying Post objects in the database.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * Retrieves all posts by the user with the given username, ordered by creation timestamp in descending order.
     *
     * @param username the username of the user
     * @return an optional set of posts by the user, ordered by creation timestamp in descending order
     */
    Optional<Set<Post>> findAllByUser_UsernameOrderByCreatedAtDesc(String username);

    /**
     * Retrieves a page of posts created by the users in the given set, using pagination.
     *
     * @param users    the set of users
     * @param pageable the pageable object specifying the page number and size
     * @return a page of posts created by the users
     */
    Page<Post> findAllByUserIn(Set<User> users, Pageable pageable);
}
