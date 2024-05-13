package com.dailype.assignment.service.Impl;

import com.dailype.assignment.model.User;
import com.dailype.assignment.repository.UserRepository;
import com.dailype.assignment.service.UserService;
import com.dailype.assignment.service.UserValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserValidatorService userValidatorService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> createUser(User user) {

        String validated_response = userValidatorService.validateUser(user);

        if(!validated_response.equals("Successfully validated")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validated_response);
        }

        user.setUser_id(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);
        user.setActive(true);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

}
