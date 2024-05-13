package com.dailype.assignment.service.Impl;

import com.dailype.assignment.model.User;
import com.dailype.assignment.pojo.enums.Status;
import com.dailype.assignment.pojo.enums.UserDetails;
import com.dailype.assignment.pojo.request.CreateUserRequest;
import com.dailype.assignment.pojo.response.CreateUserResponse;
import com.dailype.assignment.service.ManagerService;
import com.dailype.assignment.service.UserValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserValidatorServiceImpl implements UserValidatorService {

    @Autowired
    private ManagerService managerService;

    // Regular expression for a valid PAN number
    private static final String PAN_REGEX = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

    // Regular expression for a valid 10-digit mobile number
    private static final String MOBILE_NUMBER_REGEX = "(0|\\+91)?[7-9][0-9]{9}";

    @Override
    public CreateUserResponse validateUser(CreateUserRequest createUserRequest) {

        CreateUserResponse createUserResponse = new CreateUserResponse();
        User incorrect_user_detail = new User();

        if (!validateFullName(createUserRequest.getFull_name())) {
            incorrect_user_detail.setFullName(createUserRequest.getFull_name());

            createUserResponse.setStatus(Status.FAILED);
            createUserResponse.setUserDetails(UserDetails.FULL_NAME_CANNOT_BE_EMPTY);
            createUserResponse.setUser(incorrect_user_detail);

            return createUserResponse;
        }

        if (!validateMobileNumber(createUserRequest.getMob_num())) {
            incorrect_user_detail.setMobNum(createUserRequest.getMob_num());

            createUserResponse.setStatus(Status.FAILED);
            createUserResponse.setUserDetails(UserDetails.INVALID_MOBILE_NUMBER);
            createUserResponse.setUser(incorrect_user_detail);

            return createUserResponse;
        }

        if (!validatePanNumber(createUserRequest.getPan_num())) {
            incorrect_user_detail.setPanNum(createUserRequest.getPan_num());

            createUserResponse.setStatus(Status.FAILED);
            createUserResponse.setUserDetails(UserDetails.INVALID_PAN_NUMBER);
            createUserResponse.setUser(incorrect_user_detail);

            return createUserResponse;
        }

        if (!validateManagerId(createUserRequest.getManager_id())) {
            incorrect_user_detail.setManagerId(createUserRequest.getManager_id());

            createUserResponse.setStatus(Status.FAILED);
            createUserResponse.setUserDetails(UserDetails.INVALID_MANAGER_ID);
            createUserResponse.setUser(incorrect_user_detail);

            return createUserResponse;
        }

        createUserResponse.setStatus(Status.SUCCESS);
        createUserResponse.setUserDetails(UserDetails.INSERTED_ALL_FIELDS);
        return createUserResponse;
    }

    @Override
    public boolean validateFullName(String fullName) {
        return fullName != null && !fullName.trim().isEmpty();
    }

    @Override
    public boolean validateMobileNumber(String mobNum) {
        return mobNum != null && mobNum.matches(MOBILE_NUMBER_REGEX);
    }

    @Override
    public boolean validatePanNumber(String panNum) {
        return panNum != null && panNum.toUpperCase().matches(PAN_REGEX);
    }

    @Override
    public boolean validateManagerId(UUID managerId) {
        return managerService.isManagerActive(managerId);
    }


    @Override
    public String alterMobNo(String mobNum) {
        String digitsOnly = mobNum.replaceAll("\\D", "");

        // Adjust the number based on prefixes
        if (digitsOnly.startsWith("0")) {
            // Remove leading zero
            digitsOnly = digitsOnly.substring(1);
        } else if (digitsOnly.startsWith("91")) {
            // Remove country code "91"
            digitsOnly = digitsOnly.substring(2);
        }

        // Ensure the number has exactly 10 digits
        if (digitsOnly.length() == 10) {
            return digitsOnly;
        } else {
            // Handle invalid mobile number
            throw new IllegalArgumentException("Invalid mobile number");
        }
    }
}
