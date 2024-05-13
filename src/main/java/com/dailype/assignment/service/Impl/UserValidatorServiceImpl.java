package com.dailype.assignment.service.Impl;

import com.dailype.assignment.model.User;
import com.dailype.assignment.service.UserValidatorService;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.UUID;

@Service
public class UserValidatorServiceImpl implements UserValidatorService {

    // Regular expression for a valid PAN number
    private static final String PAN_REGEX = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

    // Regular expression for a valid 10-digit mobile number
    private static final String MOBILE_NUMBER_REGEX = "(0|\\+91)?[7-9][0-9]{9}";

    @Override
    public String validateUser(User user) {
        if (!validateFullName(user.getFull_name())) {
            return "Full name cannot be empty";
        }

        if (!validateMobileNumber(user.getMob_num())) {
            return "Invalid mobile number";
        }

        if (!validatePanNumber(user.getPan_num())) {
            return "Invalid PAN number";
        }

        // For manager ID validation, you would typically check against the manager table
        // and ensure that the manager ID exists and is active. Since we don't have access
        // to your manager table implementation, I'll provide a basic stub implementation.

        // Assuming a method like validateManagerExists(UUID managerId) is available in a ManagerService
        // if (!managerService.validateManagerExists(user.getManager_id())) {
        //     return "Invalid manager ID";
        // }

        return "Successfully validated";
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
        // Assuming manager ID validation is handled elsewhere
        return true; // Placeholder
    }
}
