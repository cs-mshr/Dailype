package com.dailype.assignment.controller;

import com.dailype.assignment.model.User;
import com.dailype.assignment.pojo.request.GetUserRequest;
import com.dailype.assignment.pojo.response.CreateUserResponse;
import com.dailype.assignment.pojo.response.GetUserResponse;
import com.dailype.assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create_user")
    public CreateUserResponse createUser(@RequestBody User user) {
        CreateUserResponse createUserResponse = userService.createUser(user);
        return createUserResponse;
    }

    @PostMapping("/get_users")
    public GetUserResponse getUsers(@RequestBody(required = false) User user) {
        GetUserResponse getUserResponse = userService.getUsers(user);
        return getUserResponse;
    }



//    @Autowired
//    private UserService userService;

//    @Autowired
//    private UserRepository userRepository;
//
//    @PostMapping("/get_users")
//    public ResponseEntity<?> getUsers(@RequestBody(required = false) User userRequest) {
//        List<User> users;
//
//         If userRequest is null, fetch all users
//        if (userRequest == null) {
//            users = userRepository.findAll();
//        } else {
//            UUID userId = userRequest.getUser_id();
//            UUID managerId = userRequest.getManager_id();
//            String mobNum = userRequest.getMob_num();
//
//             If userId is provided, fetch user by userId
//            if (userId != null) {
//                Optional<User> user = userRepository.findById(userId);
//                return user.map(value -> ResponseEntity.ok(Collections.singletonList(value)))
//                        .orElseGet(() -> ResponseEntity.ok(Collections.emptyList()));
//            }
//
//             If managerId is provided, fetch users by managerId
//            if (managerId != null) {
//                users = userRepository.findByManagerId(managerId);
//            } else if (mobNum != null) {
//                 If mobNum is provided, fetch user by mobNum
//                User user = userRepository.findByMobNum(mobNum);
//                if (user != null) {
//                    users = Collections.singletonList(user);
//                } else {
//                    users = Collections.emptyList();
//                }
//            } else {
//                 If no filter is provided, return all users
//                users = userRepository.findAll();
//            }
//        }
//
//         Prepare response
//        List<User> userDTOs = users.stream().map(User::new).collect(Collectors.toList());
//        return ResponseEntity.ok(Collections.singletonMap("users", userDTOs));
//    }

}
