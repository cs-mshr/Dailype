package com.dailype.assignment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Manager {

    @Id
    private UUID managerId;

    private String fullName;

    private boolean isActive;
}

