package com.socialmedia.app.controller;

import com.socialmedia.app.dto.UserDto;
import com.socialmedia.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/username/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/follow/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void followUser(@PathVariable String username, Principal principal) {
        userService.followUser(username, principal);
    }

    @DeleteMapping("/follow/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void unFollowUser(@PathVariable String username, Principal principal) {
        userService.unFollowUser(username, principal);
    }

    @GetMapping("/friendrequestsout")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> getSentFriendRequests(Principal principal) {
        return userService.getSentFriendRequests(principal);
    }

    @GetMapping("/friendrequestsin")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> getIncomingFriendRequests(Principal principal) {
        return userService.getIncomingFriendRequests(principal);
    }

    @PostMapping("/friend/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable String username, Principal principal) {
        userService.addFriend(username, principal);
    }

    @DeleteMapping("/friend/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@PathVariable String username, Principal principal) {
        userService.removeFriend(username, principal);
    }
}
