package com.dailype.assignment.repository;

import com.dailype.assignment.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ManagerRepository extends JpaRepository<Manager, UUID> {

    // Custom method to find an active manager by ID
    Optional<Manager> findByManagerIdAndIsActiveTrue(UUID managerId);

}
