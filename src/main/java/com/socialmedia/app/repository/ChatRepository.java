package com.socialmedia.app.repository;

import com.socialmedia.app.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByUser1_UsernameAndUser2_Username(String user1, String user2);
}
