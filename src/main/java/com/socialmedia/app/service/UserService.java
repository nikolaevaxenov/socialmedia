package com.socialmedia.app.service;

import com.socialmedia.app.dto.UserDto;
import com.socialmedia.app.model.FriendStatus;
import com.socialmedia.app.model.User;
import com.socialmedia.app.repository.FriendStatusRepository;
import com.socialmedia.app.repository.UserRepository;
import com.socialmedia.app.util.UserConvertor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FriendStatusRepository friendStatusRepository;
    private final UserConvertor userConvertor;

    public UserService(UserRepository userRepository, FriendStatusRepository friendStatusRepository, UserConvertor userConvertor) {
        this.userRepository = userRepository;
        this.friendStatusRepository = friendStatusRepository;
        this.userConvertor = userConvertor;
    }

    public UserDto getUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .map(userConvertor::convertToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));
    }

    public void followUser(String username, Principal principal) {
        var follower = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));

        var following = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));

        follower.followUser(following);
        userRepository.save(follower);
    }

    public void unFollowUser(String username, Principal principal) {
        var follower = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));

        var following = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));

        follower.unFollowUser(following);
        userRepository.save(follower);
    }

    public Set<String> getSentFriendRequests(Principal principal) {
        var friendStatuses = friendStatusRepository
                .findAllByUserFrom_Username(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User didn't sent friend requests!"));

        return friendStatuses
                .stream()
                .map(FriendStatus::getUserTo)
                .map(User::getUsername)
                .collect(Collectors.toSet());
    }

    public Set<String> getIncomingFriendRequests(Principal principal) {
        var friendStatuses = friendStatusRepository
                .findAllByUserTo_Username(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no incoming friend requests!"));

        return friendStatuses
                .stream()
                .map(FriendStatus::getUserFrom)
                .map(User::getUsername)
                .collect(Collectors.toSet());
    }

    public void addFriend(String username, Principal principal) {
        var toUser = friendStatusRepository.findByUserFrom_UsernameAndUserTo_Username(username, principal.getName());
        var fromUser = friendStatusRepository.findByUserFrom_UsernameAndUserTo_Username(principal.getName(), username);

        var userFrom = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));
        var userTo = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));

        if(toUser.isPresent()) {
            friendStatusRepository.delete(toUser.get());

            userFrom.addFriend(userTo);
            userTo.addFriend(userFrom);

            userFrom.followUser(userTo);

            userRepository.save(userFrom);
            userRepository.save(userTo);
        } else if (fromUser.isEmpty()) {
            if (userFrom.searchFriend(userTo)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "You are already friends with given user!");
            }
            var newFriendRequest = new FriendStatus(userFrom, userTo);
            friendStatusRepository.save(newFriendRequest);

            userFrom.followUser(userTo);
            userRepository.save(userFrom);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Friend request to given user already sent!");
        }
    }

    public void removeFriend(String username, Principal principal) {
        var toUser = friendStatusRepository.findByUserFrom_UsernameAndUserTo_Username(username, principal.getName());
        var fromUser = friendStatusRepository.findByUserFrom_UsernameAndUserTo_Username(principal.getName(), username);

        var userFrom = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));
        var userTo = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));

        if(toUser.isPresent()) {
            friendStatusRepository.delete(toUser.get());
        } else if (fromUser.isPresent()) {
            friendStatusRepository.delete(fromUser.get());

            userFrom.unFollowUser(userTo);
            userRepository.save(userFrom);
        } else if (userFrom.searchFriend(userTo)) {
            userTo.removeFriend(userFrom);
            userFrom.removeFriend(userTo);

            userFrom.unFollowUser(userTo);

            userRepository.save(userFrom);
            userRepository.save(userTo);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You are not friends with given user!");
        }
    }
}
