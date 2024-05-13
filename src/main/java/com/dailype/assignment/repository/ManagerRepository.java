package com.dailype.assignment.repository;

import com.dailype.assignment.model.Manager;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ManagerRepository extends CrudRepository<Manager, UUID> {

    // Custom method to find an active manager by ID
    Manager findByManagerIdAndIsActiveTrue(UUID managerId);
}
