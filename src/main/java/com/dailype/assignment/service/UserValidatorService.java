package com.dailype.assignment.service;

import com.dailype.assignment.model.User;
import com.dailype.assignment.pojo.request.CreateUserRequest;
import com.dailype.assignment.pojo.response.CreateUserResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserValidatorService {

    CreateUserResponse validateUser(CreateUserRequest createUserRequest);

    boolean validateFullName(String fullName);

    boolean validateMobileNumber(String mobNum);

    boolean validatePanNumber(String panNum) ;

    boolean validateManagerId(UUID managerId);

    String alterMobNo(String mobNum);

}
