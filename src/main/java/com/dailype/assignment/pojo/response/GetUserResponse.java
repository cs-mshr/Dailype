package com.dailype.assignment.pojo.response;

import com.dailype.assignment.model.User;
import lombok.Data;

import java.util.List;

@Data
public class GetUserResponse {
    List<User> users;
}
