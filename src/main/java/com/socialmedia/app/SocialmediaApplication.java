package com.socialmedia.app;

import com.socialmedia.app.model.Post;
import com.socialmedia.app.model.User;
import com.socialmedia.app.repository.PostRepository;
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
    CommandLineRunner commandLineRunner(UserRepository userRepository,
                                        PostRepository postRepository,
                                        PasswordEncoder passwordEncoder) {
        return args -> {
            var user1 = new User("user1", "user1@mail.com", passwordEncoder.encode("123456"));
            var user2 = new User("user2", "user2@mail.com", passwordEncoder.encode("qwerty"));

            userRepository.save(user1);
            userRepository.save(user2);

            var post1 = new Post("Lorem ipsum", "Dolor sit amet, consectetur adipiscing elit");
            var post2 = new Post("Ut enim ad minim veniam", "Quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat");
            var post3 = new Post("Ut enim ad minim veniam", "Quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat");
            var post4 = new Post("Ut enim ad minim veniam", "Quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat");

            post1.setUser(user1);
            post2.setUser(user1);
            post3.setUser(user2);
            post4.setUser(user2);

            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);
            postRepository.save(post4);

            //postRepository.findAllByOrderByCreatedAtDesc().forEach(System.out::println);
        };
    }

}
