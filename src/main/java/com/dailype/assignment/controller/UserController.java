package com.dailype.assignment.controller;

import com.dailype.assignment.pojo.request.*;
import com.dailype.assignment.pojo.response.*;
import com.dailype.assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CreateUserResponse createUser(@RequestBody CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest);
    }

    @PostMapping("/get_users")
    public GetUserResponse getUsers(@RequestBody(required = false) GetUserRequest getUserRequest) {
        return userService.getUsers(getUserRequest);
    }

    @PostMapping("/delete_user")
    public DeleteUserResponse deleteUser(@RequestBody(required = false) DeletUserRequest deleteUserRequest) {
        return userService.deleteUser(deleteUserRequest);
    }

    /*
    BULK-UPDATE feature to update manager_ids
    for the provided user_ids
    * */
    @PostMapping("/update_user")
    public UpdateUserResponse updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        return userService.updateUser(updateUserRequest);
    }

    @PostMapping("/fetch-employee")
    public FetchEmployeeResponse fetchEmployee(@RequestBody FetchEmployeeRequest fetchEmployeeRequest ){
        return userService.fetchEmployee(fetchEmployeeRequest);
    }


}
