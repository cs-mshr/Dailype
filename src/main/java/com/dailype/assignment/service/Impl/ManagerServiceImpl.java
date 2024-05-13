package com.dailype.assignment.service.Impl;

import com.dailype.assignment.model.Manager;
import com.dailype.assignment.repository.ManagerRepository;
import com.dailype.assignment.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public boolean isManagerActive(UUID managerId) {
        // Check if manager exists and is active
        Optional<Manager> manager = managerRepository.findByManagerIdAndIsActiveTrue(managerId);
        if(manager.isPresent()) {
            return manager.get().isActive();
        }else{
            return false;
        }
    }
}
