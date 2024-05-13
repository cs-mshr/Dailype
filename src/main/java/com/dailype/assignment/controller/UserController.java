package com.dailype.assignment.controller;

import com.dailype.assignment.model.User;
import com.dailype.assignment.repository.UserRepository;
import com.dailype.assignment.service.UserService;
import com.dailype.assignment.service.UserValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create_user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

}
