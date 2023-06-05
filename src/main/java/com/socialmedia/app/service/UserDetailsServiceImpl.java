package com.socialmedia.app.service;

import com.socialmedia.app.model.UserDetailsImpl;
import com.socialmedia.app.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class for loading user details from the UserRepository.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Constructs a UserDetailsServiceImpl with the provided UserRepository.
     *
     * @param userRepository the UserRepository to use for loading user details
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details based on the given username.
     *
     * @param username the username of the user
     * @return UserDetailsImpl containing the user details
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));
    }
}
