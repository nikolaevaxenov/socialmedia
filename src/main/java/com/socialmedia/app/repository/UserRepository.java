package com.socialmedia.app.repository;

import com.socialmedia.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entities.
 * It provides methods for CRUD operations and querying User objects in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user
     * @return an optional user object
     */
    Optional<User> findByUsername(String username);
}
