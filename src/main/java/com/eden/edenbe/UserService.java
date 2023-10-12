package com.eden.edenbe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // business logic to fetch all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // business logic to create a new user
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
