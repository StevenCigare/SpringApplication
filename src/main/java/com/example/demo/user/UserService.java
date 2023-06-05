package com.example.demo.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService{

    private final static String USER_NOT_FOUND = "user with username %s not found";
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void addUser(User user) {

        if(userRepository.findUserByEmail(user.getEmail()).isPresent()){
            throw new IllegalStateException("Email already taken!");
        }
        if(userRepository.findUserByUsername(user.getUsername()).isPresent()){
            throw new IllegalStateException("Username already taken!");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        System.out.println(userId);
        if(!userRepository.existsById(userId)){
            throw new IllegalStateException("user with id " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(String password, String username, Long userId) {
        User fetchedUser = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("user with id " + userId + " does not exist"));

        if(password != null){
            if(password.length() > 6) {
                fetchedUser.setPassword(password);
            }
            else {
                throw new IllegalStateException("Password too short!");
            }
        }

        if(username != null) {
            if(userRepository.findUserByUsername(username).isPresent()){
                throw new IllegalStateException("Username already taken");
            }
            if(username.length() > 3) {
                fetchedUser.setUsername(username);
            }
            else {
                throw new IllegalStateException("Username too short");
            }
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
    }

}
