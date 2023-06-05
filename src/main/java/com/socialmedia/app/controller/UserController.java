package com.socialmedia.app.controller;

import com.socialmedia.app.dto.UserDto;
import com.socialmedia.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

/**
 * Controller class for handling user-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    /**
     * Constructs the UserController class.
     *
     * @param userService the UserService implementation.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve.
     * @return the UserDto object representing the user.
     */
    @GetMapping("/username/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    /**
     * Follows a user.
     *
     * @param username  the username of the user to follow.
     * @param principal the authenticated principal user.
     */
    @PostMapping("/follow/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void followUser(@PathVariable String username, Principal principal) {
        userService.followUser(username, principal);
    }

    /**
     * Unfollows a user.
     *
     * @param username  the username of the user to unfollow.
     * @param principal the authenticated principal user.
     */
    @DeleteMapping("/follow/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void unFollowUser(@PathVariable String username, Principal principal) {
        userService.unFollowUser(username, principal);
    }

    /**
     * Retrieves the sent friend requests for the authenticated user.
     *
     * @param principal the authenticated principal user.
     * @return a set of usernames representing the sent friend requests.
     */
    @GetMapping("/friendrequestsout")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> getSentFriendRequests(Principal principal) {
        return userService.getSentFriendRequests(principal);
    }

    /**
     * Retrieves the incoming friend requests for the authenticated user.
     *
     * @param principal the authenticated principal user.
     * @return a set of usernames representing the incoming friend requests.
     */
    @GetMapping("/friendrequestsin")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> getIncomingFriendRequests(Principal principal) {
        return userService.getIncomingFriendRequests(principal);
    }

    /**
     * Adds or accepts a friend request for the authenticated user.
     *
     * @param username  the username of the user to add or accept as a friend.
     * @param principal the authenticated principal user.
     */
    @PostMapping("/friend/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable String username, Principal principal) {
        userService.addFriend(username, principal);
    }

    /**
     * Removes or declines a friend request for the authenticated user.
     *
     * @param username  the username of the user to remove or decline as a friend.
     * @param principal the authenticated principal user.
     */
    @DeleteMapping("/friend/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@PathVariable String username, Principal principal) {
        userService.removeFriend(username, principal);
    }
}
