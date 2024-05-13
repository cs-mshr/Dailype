package com.dailype.assignment.service;

import com.dailype.assignment.model.Manager;

import java.util.UUID;

public interface ManagerService {

    // Method to check if a manager is active
    boolean isManagerActive(UUID managerId);
}
