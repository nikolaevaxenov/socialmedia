package com.socialmedia.app.repository;

import com.socialmedia.app.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for Chat entities.
 * It provides methods for CRUD operations and querying Chat objects in the database.
 */
public interface ChatRepository extends JpaRepository<Chat, Long> {
    /**
     * Retrieves a Chat object by the usernames of user1 and user2.
     *
     * @param user1 The username of user1.
     * @param user2 The username of user2.
     * @return An Optional containing the Chat object if found, or an empty Optional if not found.
     */
    Optional<Chat> findByUser1_UsernameAndUser2_Username(String user1, String user2);
}
