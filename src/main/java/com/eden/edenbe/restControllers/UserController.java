package com.eden.edenbe.restControllers;
import com.eden.edenbe.User;
import com.eden.edenbe.UserDTO;
import com.eden.edenbe.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

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

//    @GetMapping("/{userId}/get-network")
//    public ResponseEntity<List<User>> getUserNetworkList(
//            @PathVariable Long userId
//    ) {
//        List<User> usersList = new List<User>();
//
//        User user = userService.getUserById(userId);
//        if (user != null) {
//            return ResponseEntity.ok(user);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    };

    @GetMapping("/{parent}/get-network")
    public List<UserDTO> getUsersByParentEndpoint(
            @PathVariable int parent
    ) {
        List<User> users = userService.getUsersByParent(parent);

        List<UserDTO> userDTOs = users.stream()
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(user.getId());
                    userDTO.setUsername(user.getUsername());
                    userDTO.setEmail(user.getEmail());
                    userDTO.setParent(user.getParent());
                    userDTO.setFirst_name(user.getFirst_name());
                    userDTO.setLast_name(user.getLast_name());
                    userDTO.setLeft_child(user.getLeft_child());
                    userDTO.setRight_child(user.getRight_child());
                    return userDTO;
                }).collect(Collectors.toList());
        return userDTOs;
//        return userService.getUsersByParent(parent);
    }
}
