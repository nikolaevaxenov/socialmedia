package com.socialmedia.app.repository;

import com.socialmedia.app.model.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface FriendStatusRepository extends JpaRepository<FriendStatus, Long> {
    Optional<Set<FriendStatus>> findAllByUserFrom_Username(String username);
    Optional<Set<FriendStatus>> findAllByUserTo_Username(String username);
    Optional<FriendStatus> findByUserFrom_UsernameAndUserTo_Username(String userFromUsername, String userToUsername);
}
