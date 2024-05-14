package com.dailype.assignment.pojo.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdatedDataForm {
    private String full_name;
    private String mob_num;
    private String pan_num;
    private UUID manager_id;
}
