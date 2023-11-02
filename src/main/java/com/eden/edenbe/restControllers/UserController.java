package com.eden.edenbe.restControllers;
import com.eden.edenbe.User;
import com.eden.edenbe.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    public List<User> getUsersByParentEndpoint(
            @PathVariable int parent
    ) {
        return userService.getUsersByParent(parent);
    }
}
