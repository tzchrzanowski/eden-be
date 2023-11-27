package com.eden.edenbe.restControllers;
import com.eden.edenbe.TreeNode;
import com.eden.edenbe.User;
import com.eden.edenbe.UserDTO;
import com.eden.edenbe.UserService;
import com.eden.edenbe.config.JwtUtils;
import com.eden.edenbe.packages.MoneyCalc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @GetMapping("/get-all-users")
    public List<UserDTO> getAllUsers(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        if (JwtUtils.validateToken(token) != null) {
            return userService.getAllUsersDTO();
        }
        return null;
    }

    @PatchMapping("/{userId}/update-profile-picture")
    public ResponseEntity<User> updateProfilePicture(
            @PathVariable Long userId,
            @RequestBody Map<String, String> payloadProfilePicture
    ) {
        if (JwtUtils.validateToken(payloadProfilePicture.get("token")) != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                // Gets user's profile picture URL:
                String newPictureUrl = payloadProfilePicture.get("newProfilePictureUrl");
                user.setProfile_picture_url(newPictureUrl);
                userService.updateUserProfile(user);
                return ResponseEntity.ok(user);
            } else {
                ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.badRequest().build();
    };

    /*
    * Add points to user account:
    *
    * */
    @PatchMapping("/{userId}/add-points")
    public ResponseEntity<String> addPointsToUser(
            @PathVariable Long userId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody  Map<String, Integer> addPointsPayload
    ) {
        if (JwtUtils.validateToken(token) != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                Integer pointsToAdd = addPointsPayload.get("points");
                Integer currentPoints = user.getPoints();
                Integer sumPoints = currentPoints + pointsToAdd;
                user.setPoints(sumPoints);
                userService.updateUserProfile(user);
                return ResponseEntity.ok("200");
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.badRequest().build();
    };

    /*
    * Change password of user:
    * payload object sent from frontend includes two parameters "newPassword" and "token"
    * */
    @PatchMapping("/{userId}/change-password")
    public ResponseEntity<String> updateUserPassword(
            @PathVariable Long userId,
            @RequestBody  Map<String, String> payload
    ) {
        if (JwtUtils.validateToken(payload.get("token")) != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                String newHashedPassword = userService.passwordEncoder.encode(payload.get("newPassword"));
                user.setPassword(newHashedPassword);
                userService.updateUserProfile(user);
                return ResponseEntity.ok("200");
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.badRequest().build();
    };

    /*
    * endpoint allows to create new user.
    * newUserPayload object parameters: username, email, first_name, last_name, parent, package, direct_referral
    * TODO: Potentially change setting temporary initial password to not be same hardcoded password for every new user.
    * */
    @PostMapping(value = "/add-new-user", produces = "application/json")
    public ResponseEntity<String> addNewUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody Map<String, String> newUserPayload
    ) {
        if (JwtUtils.validateToken(token) != null) {
            User newUser = new User();

            /*
            * Get direct referral user id:
            * */
            Long direct_referral_id = userService.getUserByUsername(newUserPayload.get("direct_referral")).get().getId();
            newUser.setDirect_referral(direct_referral_id);

            /*
             * first handle date of new account creation:
             * */
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedForDatabaseDateString = now.format(formatter);
            newUser.setCreation_date(formattedForDatabaseDateString);

            /*
            * Set package, money and points for new user
            * */
            MoneyCalc calculations = new MoneyCalc();
            newUser.setPackageType(newUserPayload.get("package"));
            newUser.setMoney_amount(calculations.calculatePackage(newUser.getPackageType()));
            newUser.setPoints(calculations.getInitialPointsForPackage(newUser.getPackageType()));

            /*
            * Tree structure user values:
            * */
            newUser.setParent(Integer.parseInt(newUserPayload.get("parent"))); // parent in tree, not direct_referral
            newUser.setLeft_child(null);
            newUser.setRight_child(null);

            /*
             * set remaining fields for new user:
             * */
            newUser.setUsername(newUserPayload.get("username"));
            newUser.setFirst_name(newUserPayload.get("first_name"));
            newUser.setLast_name(newUserPayload.get("last_name"));
            newUser.setActive(1); // 1 means active. 0 inactive.
            newUser.setRole_id(2); // user , not admin.
            newUser.setEmail(newUserPayload.get("email"));
            newUser.setProfile_picture_url("https://www.kindpng.com/picc/m/722-7221920_placeholder-profile-image-placeholder-png-transparent-png.png");
            newUser.setPassword("$2a$10$xgAuy8VqdA6yNn/JTGw/1eXQBrE2.H1wTyxElJSoFVVRv8w7IHaJm"); // set temporary hardcoded password.

            userService.createUser(newUser);
            Optional<User> createdUser = userService.getUserByUsername(newUser.getUsername());
            if (createdUser != null) {
                //--------------------------------------------------------------------------------------
                /*
                 * Update parent with new child id
                 * */
                User parentUser = userService.getUserById(Long.parseLong(newUserPayload.get("parent")));
                if (parentUser != null) {
                    if (parentUser.getLeft_child() == -1) {
                        parentUser.setLeft_child(createdUser.get().getId());
                    } else {
                        if (parentUser.getRight_child() == -1) {
                            parentUser.setRight_child(createdUser.get().getId());
                        }
                    }
                    userService.updateUserProfile(parentUser);
                } else {
                    return ResponseEntity.notFound().build();
                }
                //--------------------------------------------------------------------------------------

                return ResponseEntity.ok("User" + createdUser.get().getUsername() + " created");
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.badRequest().build();
    };

    @GetMapping("/{parent}/get-network")
    public TreeNode getUsersByParentEndpoint(
            @PathVariable int parent,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        String username = JwtUtils.validateToken(token);
        if(username != null) {
            if(parent > 0) {
                TreeNode userTree = buildUserTree(parent);
                return userTree;
            }
        }
        return new TreeNode();
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
        userDTO.setPoints(currentUser.getPoints());
        userDTO.setPackageType(currentUser.getPackageType());
        userDTO.setMoney_amount(currentUser.getMoney_amount());

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
}
