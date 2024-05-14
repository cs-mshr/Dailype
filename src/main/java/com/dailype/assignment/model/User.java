package com.dailype.assignment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jdk.jfr.Registered;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private UUID userId;

    private String fullName;
    private String mobNum;
    private String panNum;
    private UUID managerId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean isActive;

}
