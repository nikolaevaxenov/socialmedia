package com.socialmedia.app.controller;

import com.socialmedia.app.dto.UserDto;
import com.socialmedia.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get User by Username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserDto getUserByUsername(@Parameter(description = "The username of the user to retrieve.") @PathVariable String username) {
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
    @Operation(summary = "Follow User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User followed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public void followUser(@Parameter(description = "The username of the user to follow.") @PathVariable String username,
                           Principal principal) {
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
    @Operation(summary = "Unfollow User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User unfollowed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public void unFollowUser(@Parameter(description = "The username of the user to unfollow.") @PathVariable String username,
                             Principal principal) {
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
    @Operation(summary = "Get Sent Friend Requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sent friend requests retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User didn't sent friend requests")
    })
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
    @Operation(summary = "Get Incoming Friend Requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incoming friend requests retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User has no incoming friend requests")
    })
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
    @Operation(summary = "Add or accept friend request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend added successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "You are already friends with given user"),
            @ApiResponse(responseCode = "409", description = "Friend request to given user already sent")
    })
    public void addFriend(@Parameter(description = "The username of the friend to add.") @PathVariable String username,
                          Principal principal) {
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
    @Operation(summary = "Remove or decline friend request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend removed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "You are not friends with given user")
    })
    public void removeFriend(@Parameter(description = "The username of the friend to remove.") @PathVariable String username,
                             Principal principal) {
        userService.removeFriend(username, principal);
    }
}
