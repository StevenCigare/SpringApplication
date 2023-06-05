package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class UserConfiguration {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserConfiguration(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            User myuser = new User(
                    "Myuser",
                    bCryptPasswordEncoder.encode("password"),
                    "someemail@gmail.com",
                    UserRole.USER,
                    false,
                    true
            );
            User myuser2 = new User(
                    "Myuser2",
                    bCryptPasswordEncoder.encode("password"),
                    "someemail3@gmail.com",
                    UserRole.USER,
                    false,
                    true
            );

            userRepository.saveAll(List.of(myuser, myuser2));
        };
    }
}
