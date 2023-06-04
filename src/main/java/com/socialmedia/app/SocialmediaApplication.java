package com.socialmedia.app;

import com.socialmedia.app.model.User;
import com.socialmedia.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SocialmediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialmediaApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            var user1 = new User("user1", "user1@mail.com", passwordEncoder.encode("123456"));
            var user2 = new User("user2", "user2@mail.com", passwordEncoder.encode("qwerty"));

            userRepository.save(user1);
            userRepository.save(user2);
        };
    }

}
