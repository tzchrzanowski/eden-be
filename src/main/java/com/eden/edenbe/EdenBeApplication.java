package com.eden.edenbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class EdenBeApplication {

    public static void main(String[] args) {
        System.out.println("Eden backend server is running..");

        /*
        * Setting up context for Spring:
        * */
        ApplicationContext context = SpringApplication.run(EdenBeApplication.class, args);

        /*
        * Autowire the UserService
        * */
        UserService userService = context.getBean(UserService.class);

        /*
        * Retrieve all users from the database
        * */
        List<User> users = userService.getAllUsers();

        /*
        * Create new user:
        * */
//        User bartek = new User(2L, "Brt D", "bartek@bartek.com", "placePassword", "Bartek", "Dobraniecki", "https://...", 1, 1);
//        userService.createUser(bartek);

        /*
        * Print user information to the console
        * */
        for (User user : users) {
            if(userService.isPasswordValid("placePassword",user.getPassword())) {
                System.out.println("provided password is correct!");
            } else {
                System.out.println("Incorrect password.");
            }
            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());
            System.out.println();
        }


    }

}
