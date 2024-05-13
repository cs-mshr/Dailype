package com.dailype.assignment.repository;

import com.dailype.assignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
//    List<User> findByMob_num(String mobNum);
//    List<User> findByManagerId(UUID managerId);
//
    Optional<List<User>> findByMobNum(String mobNum);

    Optional<List<User>> findByUserId(UUID userId);

    Optional<List<User>> findByManagerId(UUID managerId);
}
