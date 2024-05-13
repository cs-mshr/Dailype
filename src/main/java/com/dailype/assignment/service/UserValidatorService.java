package com.dailype.assignment.service;

import com.dailype.assignment.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserValidatorService {

    String validateUser(User user);

    boolean validateFullName(String fullName);

    boolean validateMobileNumber(String mobNum);

    boolean validatePanNumber(String panNum) ;

    boolean validateManagerId(UUID managerId);
}
