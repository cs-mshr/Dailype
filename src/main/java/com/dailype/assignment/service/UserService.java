package com.dailype.assignment.service;

import com.dailype.assignment.model.User;
import com.dailype.assignment.pojo.request.CreateUserRequest;
import com.dailype.assignment.pojo.request.DeletUserRequest;
import com.dailype.assignment.pojo.request.GetUserRequest;
import com.dailype.assignment.pojo.response.CreateUserResponse;
import com.dailype.assignment.pojo.response.DeleteUserResponse;
import com.dailype.assignment.pojo.response.GetUserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    CreateUserResponse createUser(CreateUserRequest createUserRequest);

    GetUserResponse getUsers(GetUserRequest getUserRequest);

    DeleteUserResponse deleteUser(DeletUserRequest deleteUserRequest);

}
