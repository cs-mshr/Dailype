package com.dailype.assignment.service.Impl;

import com.dailype.assignment.model.User;
import com.dailype.assignment.pojo.enums.UserDetails;
import com.dailype.assignment.pojo.response.CreateUserResponse;
import com.dailype.assignment.pojo.response.GetUserResponse;
import com.dailype.assignment.repository.UserRepository;
import com.dailype.assignment.service.UserService;
import com.dailype.assignment.service.UserValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserValidatorService userValidatorService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CreateUserResponse createUser(User user) {

        CreateUserResponse createUserResponse = userValidatorService.validateUser(user);

        if(!createUserResponse.getUserDetails().equals(UserDetails.INSERTED_ALL_FIELDS)){
            return createUserResponse;
        }

        user.setMobNum(userValidatorService.alterMobNo(user.getMobNum()));
        user.setUserId(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);
        user.setActive(true);

        userRepository.save(user);

        createUserResponse.setUser(user);
        return createUserResponse;
    }

    @Override
    public GetUserResponse getUsers(User user) {

        GetUserResponse getUserResponse = new GetUserResponse();
        Optional<List<User>> users;

        if(user == null){
            getUserResponse.setUsers(userRepository.findAll());
            return getUserResponse;
        }

        if(user.getMobNum() != null){
            users = userRepository.findByMobNum(user.getMobNum());
            getUserResponse.setUsers(users.orElse(Collections.emptyList()));
        }

        if(user.getUserId() != null){
            users = userRepository.findByUserId(user.getUserId());
            getUserResponse.setUsers(users.orElse(Collections.emptyList()));
        }

        if(user.getManagerId() != null){
            users = userRepository.findByManagerId(user.getManagerId());
            getUserResponse.setUsers(users.orElse(Collections.emptyList()));
        }

        return getUserResponse;
    }

}
