package com.example.demo.registration;


import com.example.demo.user.User;
import com.example.demo.user.UserRole;
import com.example.demo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserService userService;

    @Autowired
    public RegistrationService(UserService userService) {
        this.userService = userService;
    }

    public String registerUser(RegistrationRequest request) {
        userService.addUser(
                new User(
                        request.getUsername(),
                        request.getPassword(),
                        request.getEmail(),
                        UserRole.USER,
                        false,
                        true
                )
        );

        //TODO: send token
        return "User added";
    }
}
