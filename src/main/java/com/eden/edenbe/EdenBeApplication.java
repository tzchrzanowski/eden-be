package com.eden.edenbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class EdenBeApplication {

    public static void main(String[] args) {
        System.out.println("started..");

        ApplicationContext context = SpringApplication.run(EdenBeApplication.class, args);

        // Autowire the UserService
        UserService userService = context.getBean(UserService.class);

        // Retrieve all users from the database
        List<User> users = userService.getAllUsers();

        // Print user information to the console
        for (User user : users) {
            System.out.println("User ID: " + user.getId());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());
            System.out.println();
        }
    }

}
