package com.dailype.assignment.service.Impl;

import com.dailype.assignment.model.User;
import com.dailype.assignment.pojo.enums.Status;
import com.dailype.assignment.pojo.enums.UserDetails;
import com.dailype.assignment.pojo.request.*;
import com.dailype.assignment.pojo.response.*;
import com.dailype.assignment.repository.UserRepository;
import com.dailype.assignment.service.UserService;
import com.dailype.assignment.service.UserValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserValidatorService userValidatorService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {

        ValidateUserDetailsRequest validateUserDetailsRequest = new ValidateUserDetailsRequest();
        convertCreateRequestTOValidateRequest(createUserRequest,validateUserDetailsRequest);

        ValidateUserDetailsResponse validateUserDetailsResponse = userValidatorService.validateUser(validateUserDetailsRequest);

        CreateUserResponse createUserResponse = new CreateUserResponse();
        convertValidateResponseTOCreateResponse(validateUserDetailsResponse,createUserResponse);

        if(!validateUserDetailsResponse.getUserDetails().equals(UserDetails.INSERTED_ALL_FIELDS)){
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

    private void convertValidateResponseTOCreateResponse(ValidateUserDetailsResponse validateUserDetailsResponse, CreateUserResponse createUserResponse) {
        createUserResponse.setStatus(validateUserDetailsResponse.getStatus());
        createUserResponse.setUserDetails(validateUserDetailsResponse.getUserDetails());
        createUserResponse.setUser(validateUserDetailsResponse.getUser());
    }

    private void convertCreateRequestTOValidateRequest(CreateUserRequest createUserRequest, ValidateUserDetailsRequest validateUserDetailsRequest) {
        validateUserDetailsRequest.setFull_name(createUserRequest.getFull_name());
        validateUserDetailsRequest.setManager_id(createUserRequest.getManager_id());
        validateUserDetailsRequest.setPan_num(createUserRequest.getPan_num());
        validateUserDetailsRequest.setMob_num(createUserRequest.getMob_num());
    }

    @Override
    public GetUserResponse getUsers(GetUserRequest getUserRequest) {
        GetUserResponse getUserResponse = new GetUserResponse();
        List<User> users;

        if (getUserRequest.getUser_id()==null && getUserRequest.getMob_num()==null && getUserRequest.getManager_id()==null) {
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

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest) {
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        List<String> errors = new ArrayList<>();

        // Check if user_ids exist in the database
        List<UUID> user_ids = updateUserRequest.getUser_ids();
        for (UUID userId : user_ids) {
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                errors.add("ID: " + userId + " NOT FOUND");
            }
        }

        // If any user_id is not found, return error response
        if (!errors.isEmpty()) {
            updateUserResponse.setStatus(Status.FAILED);
            updateUserResponse.setErrors(errors);
            return updateUserResponse;
        }

        // Validate update_data
        /*
        TODO: Response for which manger_id validation failed.
        */
        if (!userValidatorService.isValidUpdateData(updateUserRequest.getUpdate_data())) {
            updateUserResponse.setStatus(Status.FAILED);
            updateUserResponse.setMessage("Invalid update data. Please provide only valid keys.");
            return updateUserResponse;
        }

        // Update users with validated update_data
        for (UUID userId : user_ids) {
            User user = userRepository.findByUserId(userId).get();
            user = updateUser(user, updateUserRequest.getUpdate_data());
            updateUserResponse.getUpdatedUsers().add(user);
        }

        updateUserResponse.setStatus(Status.SUCCESS);
        updateUserResponse.setMessage("Users updated successfully.");
        return updateUserResponse;
    }

    private User updateUser(User user, Map<String, Object> updateData) {
        User updateUser = new User();
        if(user.getManagerId() != null){
            user.setActive(false);

            updateUser.setManagerId(user.getManagerId());

            updateUser.setActive(true);
            updateUser.setUpdatedAt(LocalDateTime.now());
            updateUser.setCreatedAt(LocalDateTime.now());
            updateUser.setUserId(UUID.randomUUID());

            updateUser.setPanNum(user.getPanNum());
            updateUser.setMobNum(user.getMobNum());
            updateUser.setFullName(user.getFullName());

            userRepository.save(user);
            return userRepository.save(updateUser);
        }

        if(updateData.containsKey("manager_id")) {
            String newManagerId = (String) updateData.get("manager_id");
            user.setManagerId(UUID.fromString(newManagerId));
        }
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

}
