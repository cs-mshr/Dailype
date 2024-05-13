package com.dailype.assignment.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class Manager {

    @Id
    private UUID managerId;

    private String fullName;

    private boolean isActive;
}

