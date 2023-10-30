package com.eden.edenbe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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
    private PasswordEncoder passwordEncoder;

    /*
    * getting all users list
    * */
    public List<User> getAllUsers() {
        return userRepository.findAll();
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
