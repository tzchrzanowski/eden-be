package com.eden.edenbe.restControllers;
import com.eden.edenbe.TreeNode;
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

/*
* Previous endpoint for /id/get-network
* TODO: to be removed, works along with recursiveFetchUsers(), allows to return list of descendants of current user.
* */
//    @GetMapping("/{parent}/get-network")
//    public List<UserDTO> getUsersByParentEndpoint(
//            @PathVariable int parent
//    ) {
//        List<UserDTO> userDTOs = new ArrayList<>();
//
//        int totalUsers = userService.getTotalUsers();
//        int maxDepth = calculateMaxDepth(totalUsers);
//        recursiveFetchUsers(parent, userDTOs, 0, maxDepth);
//        return userDTOs;
//    }

    @GetMapping("/{parent}/get-network")
    public TreeNode getUsersByParentEndpoint(
            @PathVariable int parent
    ) {
        if(parent > 0) {
            TreeNode userTree = buildUserTree(parent);
            return userTree;
        }
        return new TreeNode();
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
    * TODO: potentially to be removed. returns list of all children descendants of current node ( without current user ).
    * recursively goes through users to check if there are more users to be added as children.
    * updates the referenced object userDTOs list, so it does not need to return it.
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

    /*
    * Recursively builds binary-tree structure based on given parent user id:
    * */
    private TreeNode buildUserTree(int parent) {
        List<User> users = userService.getUsersByParent(parent);

        Long userIdAsLong = Long.valueOf(parent);
        User currentUser = userService.getUserById(userIdAsLong);

        /*
         * setting up the user object for response:
         * */
        UserDTO userDTO = new UserDTO();
        userDTO.setId(currentUser.getId());
        userDTO.setUsername(currentUser.getUsername());
        userDTO.setEmail(currentUser.getEmail());
        userDTO.setParent(currentUser.getParent());
        userDTO.setFirst_name(currentUser.getFirst_name());
        userDTO.setLast_name(currentUser.getLast_name());
        userDTO.setLeft_child(currentUser.getLeft_child());
        userDTO.setRight_child(currentUser.getRight_child());
        userDTO.setProfile_picture_url(currentUser.getProfile_picture_url());

        /*
        * Setting up the TreeNode object of current user along with its child nodes recursively:
        * */
        TreeNode node = new TreeNode(parent, null);
        node.setId(parent);
        node.setUser(userDTO);

        TreeNode leftChild = null;
        if (currentUser.getLeft_child() > 0) {
            for (User user : users) {
                if (user.getId() == currentUser.getLeft_child()) {
                    leftChild = buildUserTree(currentUser.getLeft_child().intValue());
                }
            }
        }

        TreeNode rightChild = null;
        if (currentUser.getRight_child() > 0) {
            for (User user : users) {
                if (user.getId() == currentUser.getRight_child()) {
                    rightChild = buildUserTree(currentUser.getRight_child().intValue());
                }
            }
        }

        node.setLeft(leftChild);
        node.setRight(rightChild);
        return node;
    }
}
