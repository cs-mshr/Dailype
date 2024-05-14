package com.dailype.assignment.pojo.request;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class UpdateUserRequest {
    private List<UUID> user_ids;
    private UpdatedDataForm update_data;
}

