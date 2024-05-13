package com.dailype.assignment.service.Impl;

import com.dailype.assignment.model.User;
import com.dailype.assignment.pojo.enums.Status;
import com.dailype.assignment.pojo.enums.UserDetails;
import com.dailype.assignment.pojo.request.CreateUserRequest;
import com.dailype.assignment.pojo.request.DeletUserRequest;
import com.dailype.assignment.pojo.request.GetUserRequest;
import com.dailype.assignment.pojo.response.CreateUserResponse;
import com.dailype.assignment.pojo.response.DeleteUserResponse;
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
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {

        CreateUserResponse createUserResponse = userValidatorService.validateUser(createUserRequest);

        if(!createUserResponse.getUserDetails().equals(UserDetails.INSERTED_ALL_FIELDS)){
            return createUserResponse;
        }

        User user = new User();

        //Setting validated data from requestFORM
        user.setFullName(createUserRequest.getFull_name());
        user.setManagerId(createUserRequest.getManager_id());
        user.setPanNum(createUserRequest.getPan_num());

        user.setMobNum(userValidatorService.alterMobNo(createUserRequest.getMob_num()));
        user.setUserId(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);
        user.setActive(true);

        userRepository.save(user);

        createUserResponse.setUser(user);
        return createUserResponse;
    }

    @Override
    public GetUserResponse getUsers(GetUserRequest getUserRequest) {
        GetUserResponse getUserResponse = new GetUserResponse();
        List<User> users;

        if (getUserRequest == null) {
            users = userRepository.findAll();
        } else if (getUserRequest.getMob_num() != null) {
            Optional<User> user = userRepository.findByMobNum(getUserRequest.getMob_num());
            users = user.map(Collections::singletonList).orElse(Collections.emptyList());
        } else if (getUserRequest.getUser_id() != null) {
            Optional<User> user = userRepository.findByUserId(getUserRequest.getUser_id());
            users = user.map(Collections::singletonList).orElse(Collections.emptyList());
        } else if (getUserRequest.getManager_id() != null) {
            users = userRepository.findByManagerId(getUserRequest.getManager_id()).orElse(Collections.emptyList());
        } else {
            users = Collections.emptyList();
        }

        getUserResponse.setUsers(users);
        return getUserResponse;
    }

    @Override
    public DeleteUserResponse deleteUser(DeletUserRequest deleteUserRequest) {
        DeleteUserResponse deleteUserResponse = new DeleteUserResponse();

        if(deleteUserRequest.getMob_num() != null){
            String mobNum = deleteUserRequest.getMob_num();
            Optional<User> user = userRepository.findByMobNum(mobNum);

            if (user.isPresent()) {
                userRepository.delete(user.get());
                deleteUserResponse.setStatus(Status.SUCCESS);
                deleteUserResponse.setMob_num(mobNum);
            } else {
                deleteUserResponse.setStatus(Status.NOT_FOUND);
                deleteUserResponse.setMob_num(mobNum);
            }
            return deleteUserResponse;
        }

        if(deleteUserRequest.getUser_id() != null){
            UUID userId = UUID.fromString(deleteUserRequest.getUser_id());
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                userRepository.delete(userOptional.get());
                deleteUserResponse.setStatus(Status.SUCCESS);
                deleteUserResponse.setUser_id(userId);
            } else {
                deleteUserResponse.setStatus(Status.NOT_FOUND);
                deleteUserResponse.setUser_id(userId);
            }
            return deleteUserResponse;
        }

        deleteUserResponse.setStatus(Status.FAILED);
        deleteUserResponse.setError_message("Please provide either user_id or mob_num.");
        return deleteUserResponse;
    }

}
