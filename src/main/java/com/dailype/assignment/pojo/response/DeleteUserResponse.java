package com.dailype.assignment.pojo.response;

import com.dailype.assignment.pojo.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteUserResponse {
    private Status status;
    private UUID user_id;
    private String mob_num;
    private String error_message;
}
