package com.dailype.assignment.service.Impl;

import com.dailype.assignment.model.User;
import com.dailype.assignment.repository.ManagerRepository;
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

        if (!validateManagerId(user.getManager_id())) {
            return "Invalid manager ID";
        }

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
