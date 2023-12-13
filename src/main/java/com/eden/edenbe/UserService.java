package com.eden.edenbe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    /*
    * User repository from spring that automatically connects to DB
    * */
    @Autowired
    private UserRepository userRepository;

    /*
    * Password encoder from spring security that helps encode passwords
    * */
    @Autowired
    public PasswordEncoder passwordEncoder;

    /*
    * getting all users list
    * */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /*
    * getting all users list with limited attributes:
    * returns only users that are not an accountants
    * */
    public List<UserDTO> getAllUsersDTO() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
                .filter(user -> !isUserAnAccountant(user))
                .map(user -> mapToUserDTO(user))
                .collect(Collectors.toList());

        return userDTOs;
    }

    /*
    * get all users that are directly referred by given user as their parent
    * */
    public List<UserDTO> getUsersDTOByDirectReferral(Long direct_referral) {
        List<User> referredUsers = getUsersByDirectReferral(direct_referral);
        List<UserDTO> referredUsersDTOs = referredUsers.stream()
                .map(referredUser -> mapToUserDTO(referredUser))
                .collect(Collectors.toList());

        return referredUsersDTOs;
    };

    /*
     * Get pairs for referral:
     * */
    public List<User> getPerfectPairsOfDirectReferral(List<User> referrals) {
        Set<Long> pairsLongIdsSet = new HashSet<>();
        for (User user : referrals) {
            Long parentIdOfCurrentUser = Long.valueOf(user.getParent());
            User parent = getUserById(parentIdOfCurrentUser);
            if (parent.getLeft_child() > -1) {
                Long leftChildId = parent.getLeft_child();
                User leftChild = getUserById(leftChildId);
                if (parent.getRight_child() > -1) {
                    Long rightChildId = parent.getRight_child();
                    User rightChild = getUserById(rightChildId);
                    if (leftChild.getDirect_referral() == rightChild.getDirect_referral()) {
                        if (!pairsLongIdsSet.contains(leftChild.getId())) {
                            pairsLongIdsSet.add(leftChild.getId());
                        }
                        if (!pairsLongIdsSet.contains(rightChild.getId())) {
                            pairsLongIdsSet.add(rightChild.getId());
                        }
                    }
                }
            }
        }
        List<User> pairsList = new ArrayList<User>();
        for (Long element : pairsLongIdsSet) {
            pairsList.add(getUserById(element));
        }
        return pairsList;
    }

    /*
    * Returns single user DTO
    * */
    public UserDTO getUserDTO(User user) {
        UserDTO userDTO = mapToUserDTO(user);
        return userDTO;
    }

    /*
    * returns all cash-out users as userDTO
    * */
    public List<UserDTO> getAllCashOutUsersDTO() {
        List<User> users = userRepository.findAll();
        List<UserDTO> cashOutUsersDTOs = users.stream()
                .filter(user -> doesUserWantToCashOut(user))
                .map(user -> mapToUserDTO(user))
                .collect(Collectors.toList());
        return cashOutUsersDTOs;
    }

    /*
    * Checks if user is an accountant:
    * */
    private boolean isUserAnAccountant(User user) {
        return user.getRole_id() == 3;
    }

    /*
     * Checks if user wants to cash out
     * */
    private boolean doesUserWantToCashOut(User user) {
        return user.getCashOut() == true;
    }

    /*
    * method used by getAllUsersDTO to re-map selected user attributes into UserDTO user
    * */
    private UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirst_name(user.getFirst_name());
        userDTO.setLast_name(user.getLast_name());
        userDTO.setProfile_picture_url(user.getProfile_picture_url());
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setParent(user.getParent());
        userDTO.setLeft_child(user.getLeft_child());
        userDTO.setRight_child(user.getRight_child());
        userDTO.setPoints(user.getPoints());
        userDTO.setMonthly_points(user.getMonthly_points());
        userDTO.setPackageType(user.getPackageType());
        userDTO.setMoney_amount(user.getMoney_amount());
        userDTO.setRole_id(user.getRole_id());
        userDTO.setDirect_referral(user.getDirect_referral());
        userDTO.setCashOut(user.getCashOut());
        userDTO.setCash_out_details(user.getCash_out_details());
        userDTO.setPairs_amount(user.getPairs_amount());
        userDTO.setGift_certificates_amount(user.getGift_certificates_amount());
        return userDTO;
    }

    /*
    * returns total amount of users
    * */
    public int getTotalUsers() {
        Long totalAmountAsLong = userRepository.count();
        int totalUsersAsInt = totalAmountAsLong.intValue();
        return totalUsersAsInt;
    }

    /*
    * Creating new user
    * */
    public User createUser(User user) {
        /*
        * Hashing the user's password before storing it into db:
        * */
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    /*
    * Get user by id:
    * */
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /*
     * Get user by username:
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /*
    * Get users by parent ID:
    * */
    public List<User> getUsersByParent(int parent) {
        return userRepository.findByParent(parent);
    }

    /*
    * Get users by user that directly referred them into network:
    */
    public List<User> getUsersByDirectReferral(Long direct_referral) {
        return userRepository.findByDirectReferral(direct_referral);
    }

    /*
    * Update user profile:
    * */
    public void updateUserProfile(User user) {
        // TODO: extend user profile logic here.
        userRepository.save(user);
    }

    /*
    * Method to verify if provided password is correct:
    * */
    public boolean isPasswordValid(String enteredPassword, String storedHashedPassword) {
        return passwordEncoder.matches(enteredPassword, storedHashedPassword);
    }
}
