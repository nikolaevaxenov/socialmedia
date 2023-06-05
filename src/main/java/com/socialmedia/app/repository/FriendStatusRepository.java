package com.socialmedia.app.repository;

import com.socialmedia.app.model.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for FriendStatus entities.
 * It provides methods for CRUD operations and querying FriendStatus objects in the database.
 */
public interface FriendStatusRepository extends JpaRepository<FriendStatus, Long> {
    /**
     * Retrieves a Set of FriendStatus objects where the user is the userFrom with the given username.
     *
     * @param username The username of the userFrom.
     * @return An Optional containing a Set of FriendStatus objects if found, or an empty Optional if not found.
     */
    Optional<Set<FriendStatus>> findAllByUserFrom_Username(String username);

    /**
     * Retrieves a Set of FriendStatus objects where the user is the userTo with the given username.
     *
     * @param username The username of the userTo.
     * @return An Optional containing a Set of FriendStatus objects if found, or an empty Optional if not found.
     */
    Optional<Set<FriendStatus>> findAllByUserTo_Username(String username);

    /**
     * Retrieves a FriendStatus object based on the usernames of userFrom and userTo.
     *
     * @param userFromUsername The username of the userFrom.
     * @param userToUsername   The username of the userTo.
     * @return An Optional containing the FriendStatus object if found, or an empty Optional if not found.
     */
    Optional<FriendStatus> findByUserFrom_UsernameAndUserTo_Username(String userFromUsername, String userToUsername);
}
