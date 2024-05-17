package com.dailype.assignment.service;

import com.dailype.assignment.pojo.request.*;
import com.dailype.assignment.pojo.response.*;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    CreateUserResponse createUser(CreateUserRequest createUserRequest);

    GetUserResponse getUsers(GetUserRequest getUserRequest);

    DeleteUserResponse deleteUser(DeletUserRequest deleteUserRequest);

    UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest);

    FetchEmployeeResponse fetchEmployee(FetchEmployeeRequest fetchEmployeeRequest);
}
