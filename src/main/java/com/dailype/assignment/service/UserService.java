package com.dailype.assignment.service;

import com.dailype.assignment.model.User;
import com.dailype.assignment.pojo.response.CreateUserResponse;
import com.dailype.assignment.pojo.response.GetUserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    CreateUserResponse createUser(User user);

    GetUserResponse getUsers(User user);
//
//    List<User> getAllUsers();
}
