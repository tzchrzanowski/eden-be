package com.eden.edenbe.restControllers;
import com.eden.edenbe.User;
import com.eden.edenbe.UserDTO;
import com.eden.edenbe.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/public/users")
public class UserController {
//    private final Authentication auth;

    @Autowired
    private UserService userService;

//    @Autowired
//    public UserController(Authentication authentication) {
//        this.auth = authentication;
//    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/{userId}/update-profile-picture")
    public ResponseEntity<User> updateProfilePicture(
            @PathVariable Long userId,
            @RequestBody String newProfilePictureUrl
    ) {
        User user = userService.getUserById(userId);
        if (user != null) {
            // Gets user's profile picture URL:
            user.setProfile_picture_url(newProfilePictureUrl);
            userService.updateUserProfile(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    };

//    @GetMapping("/{parent}/get-network")
//    public List<UserDTO> getUsersByParentEndpoint(
//            @PathVariable int parent
//    ) {
//        List<User> users = userService.getUsersByParent(parent);
//
//        List<UserDTO> userDTOs = users.stream()
//                .map(user -> {
//                    UserDTO userDTO = new UserDTO();
//                    userDTO.setId(user.getId());
//                    userDTO.setUsername(user.getUsername());
//                    userDTO.setEmail(user.getEmail());
//                    userDTO.setParent(user.getParent());
//                    userDTO.setFirst_name(user.getFirst_name());
//                    userDTO.setLast_name(user.getLast_name());
//                    userDTO.setLeft_child(user.getLeft_child());
//                    userDTO.setRight_child(user.getRight_child());
//                    return userDTO;
//                }).collect(Collectors.toList());
//        return userDTOs;
//    }

    @GetMapping("/{parent}/get-network")
    public List<UserDTO> getUsersByParentEndpoint(
            @PathVariable int parent
    ) {
        List<UserDTO> userDTOs = new ArrayList<>();

        int totalUsers = userService.getTotalUsers();
        int maxDepth = calculateMaxDepth(totalUsers);
        recursiveFetchUsers(parent, userDTOs, 0, maxDepth);
        return userDTOs;
    }

    /*
    * Calculate the max depth based on the number of users
    * TODO: Will not be efficient for users that are not having many children since is based on total amount of users at this point.
    * */
    private int calculateMaxDepth(int totalUsers) {
        int maxDepth = (int) (Math.log(totalUsers + 1) / Math.log(2));
        return maxDepth;
    }

    /*
    * recursively goes through users to check if there are more users to be added as children.
    * */
    private void recursiveFetchUsers(int parent, List<UserDTO> userDTOs, int depth, int maxDepth) {
        if (depth >= maxDepth) {
            return;
        }

        List<User> users = userService.getUsersByParent(parent);

        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setParent(user.getParent());
            userDTO.setFirst_name(user.getFirst_name());
            userDTO.setLast_name(user.getLast_name());
            userDTO.setLeft_child(user.getLeft_child());
            userDTO.setRight_child(user.getRight_child());
            userDTOs.add(userDTO);

            int userIdAsInt = user.getId().intValue();

            // Recursively fetch child users, increasing depth
            recursiveFetchUsers(userIdAsInt, userDTOs, depth + 1, maxDepth);
        }
    }
}
