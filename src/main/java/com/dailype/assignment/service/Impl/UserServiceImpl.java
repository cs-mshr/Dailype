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

        UpdatedDataForm updatedDataForm = updateUserRequest.getUpdate_data();

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

        if(updateUserRequest.getUser_ids().size() == 1){
            UUID user_id = updateUserRequest.getUser_ids().get(0);
            Optional<User> userOptional = userRepository.findById(user_id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                //Validating user update-data
                ValidateUserDetailsResponse validateUserDetailsResponse
                        = userValidatorService.userDetailsForUpdate(
                                updateUserRequest.getUpdate_data()
                            );

                if(!validateUserDetailsResponse.getUserDetails().equals(UserDetails.ALL_FIELDS_ARE_VALID)){

                    updateUserResponse.setStatus(Status.FAILED);
                    updateUserResponse.setMessage(validateUserDetailsResponse.getUserDetails().toString());
                    return updateUserResponse;
                }

                //Updating DB with updated user-fields
                if(updatedDataForm.getMob_num() != null){
                    user.setMobNum(updatedDataForm.getMob_num());
                }
                if(updatedDataForm.getPan_num() != null){
                    user.setPanNum(updatedDataForm.getPan_num());
                }
                if(updatedDataForm.getFull_name() != null){
                    user.setFullName(updatedDataForm.getFull_name());
                }

                ManagerIdUpdate(updatedDataForm, user);
                updateUserResponse.setUpdatedUsers(List.of(user));
            }

        }else if(updateUserRequest.getUser_ids().size() > 1){

            //1. if object contains any field other than manager id in bulk_update
            if(updatedDataForm.getFull_name()!=null
                    || updatedDataForm.getPan_num()!=null || updatedDataForm.getMob_num()!=null){
                updateUserResponse.setStatus(Status.FAILED);
                updateUserResponse.setMessage("extra keys present these keys cannot be updated in bulk_update, use individual update for them");
            }

            //2. validate manger id, call update manager id for each user_id
            boolean isManagerIdValid = userValidatorService.validateManagerId(
                    updatedDataForm.getManager_id()
            );

            if(!isManagerIdValid){
                updateUserResponse.setStatus(Status.FAILED);
                updateUserResponse.setMessage("Manager ID is not valid");

                return updateUserResponse;
            }

            //Updating manager-id for each user in bulk update
            for(UUID userId : user_ids){
                Optional<User> userOptional = userRepository.findById(userId);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    ManagerIdUpdate(updatedDataForm, user);
                }
            }

        }else{
            updateUserResponse.setStatus(Status.FAILED);
            updateUserResponse.setMessage("User_id not present in request");
        }

        updateUserResponse.setStatus(Status.SUCCESS);
        return updateUserResponse;
    }

    @Override
    public FetchEmployeeResponse fetchEmployee(FetchEmployeeRequest fetchEmployeeRequest) {
        UUID manager_id = fetchEmployeeRequest.getManager_id();
        Optional<User> user = userRepository.findByUserId(manager_id);

        FetchEmployeeResponse fetchEmployeeResponse = new FetchEmployeeResponse();
        List<FetchEmployeeResponse> employeesData = new ArrayList<>();
        if(user.isPresent()){
            String name = user.get().getFullName();
            fetchEmployeeResponse.setName(name);

            Optional<List<User>> child = userRepository.findByManagerId(manager_id);

            if(child.isPresent()){
                for(User childUser : child.get()){
                    employeesData.add(
                            fetchEmployee(
                                    FetchEmployeeRequest.builder()
                                            .manager_id(childUser.getUserId())
                                            .build()
                                )
                    );
                }

                fetchEmployeeResponse.setUsers(employeesData);
            }

        }

        return fetchEmployeeResponse;
    }

    private void ManagerIdUpdate(UpdatedDataForm updatedDataForm, User user) {
        if(user.getManagerId() == null || user.getManagerId() == updatedDataForm.getManager_id()){
//                if(user.getManagerId() == null){
            user.setManagerId(updatedDataForm.getManager_id());
        }
        else{
            user.setActive(false);
            User newUser = User.builder()
                    .fullName(user.getFullName())
                    .managerId(updatedDataForm.getManager_id())
                    .updatedAt(LocalDateTime.now())
                    .userId(UUID.randomUUID())
                    .isActive(true)
                    .panNum(user.getPanNum())
                    .mobNum(user.getMobNum())
                    .build();
            userRepository.save(newUser);
        }

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
