package com.dailype.assignment.pojo.response;

import com.dailype.assignment.model.User;
import com.dailype.assignment.pojo.enums.Status;
import com.dailype.assignment.pojo.enums.UserDetails;
import lombok.Data;

@Data
public class CreateUserResponse {
    private Status status;
    private UserDetails userDetails;
    private User user;
}
