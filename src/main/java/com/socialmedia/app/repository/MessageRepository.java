package com.socialmedia.app.repository;

import com.socialmedia.app.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Message entities.
 * It provides methods for CRUD operations and querying Message objects in the database.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
}
