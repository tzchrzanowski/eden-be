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

import java.math.BigDecimal;
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

    /*
     * Set cash_out parameter value for user account:
     * */
    @GetMapping("/{userId}/get_user_details")
    public UserDTO getUserDetails(
            @PathVariable Long userId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        if (JwtUtils.validateToken(token) != null) {
            User user = userService.getUserById(userId);
            UserDTO userDTO = userService.getUserDTO(user);
            if (userDTO != null) {
                return userDTO;
            }
        }
        return null;
    };

    @GetMapping("/get-all-users")
    public List<UserDTO> getAllUsers(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        if (JwtUtils.validateToken(token) != null) {
            return userService.getAllUsersDTO();
        }
        return null;
    }

    @GetMapping("/get-all-cash-out-users")
    public List<UserDTO> getAllCashOutUsers(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        if (JwtUtils.validateToken(token) != null) {
            return userService.getAllCashOutUsersDTO();
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
                Integer currentMonthlyPoints = user.getMonthly_points();
                Integer sumPoints = currentMonthlyPoints + pointsToAdd;
                user.setMonthly_points(sumPoints);
                userService.updateUserProfile(user);
                return ResponseEntity.ok("200");
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.badRequest().build();
    };

    /*
     * Set cash_out parameter value for to false on user account:
     * User by user to cancel his cash-out request
     * */
    @PatchMapping("/{userId}/set_cash_out_bool")
    public ResponseEntity<String> setCashOutForUser(
            @PathVariable Long userId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody  Map<String, Boolean> cashOutBoolPayload
    ) {
        if (JwtUtils.validateToken(token) != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                boolean new_cash_out_value = cashOutBoolPayload.get("cash_out_bool");
                user.setCashOut(new_cash_out_value);
                userService.updateUserProfile(user);
                return ResponseEntity.ok("200");
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.badRequest().build();
    };

    /*
     * Set cash_out parameter value for to false on user account:
     * User by user to cancel his cash-out request
     * */
    @PatchMapping("/{userId}/set_cash_out_by_user")
    public ResponseEntity<String> setCashOutByUser(
            @PathVariable Long userId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody  Map<String, String> cashOutByUserPayload
    ) {
        if (JwtUtils.validateToken(token) != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                String new_cash_out_details = cashOutByUserPayload.get("cash_out_details");
                user.setCash_out_details(new_cash_out_details);
                user.setCashOut(true);
                userService.updateUserProfile(user);
                return ResponseEntity.ok("200");
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.badRequest().build();
    };

    /*
     * Set cash_out parameter value for user account along with reducing the amount of money that user has:
     * User is cashed out so cash_out parameter can be directly set to false.
     * */
    @PatchMapping("/{userId}/set_cash_out_amount")
    public ResponseEntity<String> setCashOutAmountForUser(
            @PathVariable Long userId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody  Map<String, BigDecimal> cashOutAmountPayload
    ) {
        if (JwtUtils.validateToken(token) != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                BigDecimal cash_out_amount_value = cashOutAmountPayload.get("cash_out_amount");
                BigDecimal current_money = user.getMoney_amount();
                BigDecimal remaining_money = current_money.subtract(cash_out_amount_value);
                user.setMoney_amount(remaining_money);
                user.setCashOut(false);
                userService.updateUserProfile(user);
                return ResponseEntity.ok("200");
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.badRequest().build();
    };

    /*
     * Add points to user account:
     * payload has: username
     * Monthly points will go all the way to 0 regardless how many there are. all monthly points will be assigned to user points
     * */
    @PatchMapping("/add-monthly-points-for-user")
    public ResponseEntity<String> addMonthlyPointsToUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody  Map<String, String> addMonthlyPointsPayload
    ) {
        if (JwtUtils.validateToken(token) != null) {
            Optional<User> user = userService.getUserByUsername(addMonthlyPointsPayload.get("username"));
            if (user != null) {
                Integer monthlyPointsToAdd = user.get().getMonthly_points();
                Integer currentUserPoints = user.get().getPoints();
                Integer sumPoints = currentUserPoints + monthlyPointsToAdd;
                user.get().setPoints(sumPoints);
                user.get().setMonthly_points(0);
                userService.updateUserProfile(user.get());
                return ResponseEntity.ok("200");
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.badRequest().build();
    };

    /*
     * Add points to user account:
     * payload has: username
     * Monthly points will go all the way to 0 regardless how many there are. all monthly points will be assigned to user points
     * */
    @PatchMapping("/add-monthly-points-for-all-users")
    public ResponseEntity<Integer> addMonthlyPointsToAllUsers(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody  Map<String, String> addMonthlyPointsPayload
    ) {
        if (JwtUtils.validateToken(token) != null) {
            List<User> users = userService.getAllUsers();

            if (users != null) {
                for (User user : users) {
                    Integer sum = user.getPoints() + user.getMonthly_points();
                    user.setPoints(sum);
                    user.setMonthly_points(0);
                    userService.updateUserProfile(user);
                }
                return ResponseEntity.ok(200);
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
             * Set package, money and points for new user
             * Initial monthly points are always 0
             * initial money on every new account is 0
             */
            MoneyCalc calculations = new MoneyCalc();
            newUser.setPackageType(newUserPayload.get("package"));
            newUser.setMoney_amount(new BigDecimal(0));
            newUser.setCashOut(false);
            newUser.setCash_out_details("");
            newUser.setPoints(calculations.getInitialPointsForPackage(newUser.getPackageType()));
            newUser.setMonthly_points(0);
            newUser.setPairs_amount(0);

            /*
            * Get direct referral user id:
            * */
            Long direct_referral_id = userService.getUserByUsername(newUserPayload.get("direct_referral")).get().getId();
            newUser.setDirect_referral(direct_referral_id);

            /*
            * Set money bonus to direct-referral-parent for adding new user based on package type:
            * */
            User directReferralParent = userService.getUserById(direct_referral_id);
            BigDecimal directReferralParentMoney = directReferralParent.getMoney_amount();
            BigDecimal moneyToAddForReferral = calculations.getMoneyForSellingPackage(newUserPayload.get("package"));
            BigDecimal directReferralParentMoneySum = directReferralParentMoney.add(moneyToAddForReferral) ;
            directReferralParent.setMoney_amount(directReferralParentMoneySum);
            userService.updateUserProfile(directReferralParent);

            /*
             * first handle date of new account creation:
             * */
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedForDatabaseDateString = now.format(formatter);
            newUser.setCreation_date(formattedForDatabaseDateString);

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
                /*
                * Check for perfect pair creation for referral bonus:
                * */
                checkForPerfectPair(parentUser.getId(), direct_referral_id);
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
    * After new user is created and direct-parent updated with new user child node,
    * then it can be checked if newly added user created a perfect-referral-pair
    *
    * Set money bonus to direct-referral-parent for creating perfect-pair:
    * */
    private void checkForPerfectPair(Long parentUserId, Long directReferralId) {
        User parentUser = userService.getUserById(parentUserId);
        User parentLeftChild = userService.getUserById(parentUser.getLeft_child());
        User parentRightChild = userService.getUserById(parentUser.getRight_child());
        if (parentLeftChild.getDirect_referral() == parentRightChild.getDirect_referral()) {
            /*
             * if both parent children hase same referral, then reduce their 40 points
             * */
            if (parentLeftChild.getPoints() >= 40 && parentRightChild.getPoints() >= 40) {
                int leftChildNewTotalPoints = parentLeftChild.getPoints() - 40;
                int rightChildNewTotalPoints = parentRightChild.getPoints() - 40;
                parentLeftChild.setPoints(leftChildNewTotalPoints);
                parentRightChild.setPoints(rightChildNewTotalPoints);
                userService.updateUserProfile(parentLeftChild);
                userService.updateUserProfile(parentRightChild);

                /*
                *  add extra 200 php to referral-parent-user for creating perfect-pair of referral
                * */
                User referralParent = userService.getUserById(directReferralId);
                BigDecimal referralParentMoney = referralParent.getMoney_amount();
                BigDecimal moneyToAddForPerfectPairReferral = new BigDecimal(800); // 800 php for pair-match-referral bonus
                BigDecimal referralParentMoneySum = referralParentMoney.add(moneyToAddForPerfectPairReferral) ;
                referralParent.setMoney_amount(referralParentMoneySum);

                /*
                * increase amount of pairs created by parent:
                * */
                int currentPairsOfReferralParent = referralParent.getPairs_amount();
                currentPairsOfReferralParent += 1;
                referralParent.setPairs_amount(currentPairsOfReferralParent);

                userService.updateUserProfile(referralParent);
            }
        }

        /*
         * Update changes to direct-referral-parent user after completed previous two checks:
         * */
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
