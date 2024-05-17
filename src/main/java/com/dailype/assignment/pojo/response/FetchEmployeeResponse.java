package com.dailype.assignment.pojo.response;

import lombok.Data;

import java.util.List;

@Data
public class FetchEmployeeResponse {
    private String name;
    private List<FetchEmployeeResponse> users;
}
