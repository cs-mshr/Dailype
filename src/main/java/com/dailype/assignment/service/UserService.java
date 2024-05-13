package com.dailype.assignment.service;

import com.dailype.assignment.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    ResponseEntity<?> createUser(User user);

}
