package com.dailype.assignment.pojo.response;

import com.dailype.assignment.model.User;
import com.dailype.assignment.pojo.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserResponse {
    private Status status;
    private String message;
    private List<String> errors = new ArrayList<>();
    private List<User> updatedUsers = new ArrayList<>();
}
