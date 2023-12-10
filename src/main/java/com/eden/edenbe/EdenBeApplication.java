package com.eden.edenbe;

import com.eden.edenbe.config.CorsConfig;
import com.nimbusds.jose.JOSEException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.List;

@SpringBootApplication(scanBasePackages = "com.eden.edenbe")
@Import(CorsConfig.class) // includes CorsConfig class in spring boot app run
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
        String usersStr =  "All users from database: ";
        int printCount = 0;
        for (User user : users) {
            usersStr += user.getUsername();
            usersStr += "|" + user.getEmail() + ", ";
            printCount ++;
            if (printCount == 5) {
                printCount = 0;
                usersStr += "\n";
            }
            /*
            * quick check for password for user. Useless now but might come useful at some point.
            * */
//            if(userService.isPasswordValid("placePassword",user.getPassword())) {
//                System.out.println("provided password is correct!");
//            } else {
//                System.out.println("Incorrect password.");
//            }
        }
        System.out.println("\n" + usersStr + "\n");
        /*-------------------------------------------------------*/


        /*
        * checking if getting all referred users works properly:
        * */
        long userId = 2;
        List<UserDTO> referredUsersDTO = userService.getUsersDTOByDirectReferral(userId);
        String referredUsersStr =  "Referred users by: " + userId + " id : " ;
        for (UserDTO refUser : referredUsersDTO) {
            referredUsersStr += refUser.getId();
            referredUsersStr += ", ";
        }
        System.out.println("\n" + referredUsersStr + "\n");
        /*-------------------------------------------------------*/

        List<User> referredUsers = userService.getUsersByDirectReferral(userId);
        List<User> referredPairs = userService.getPairsOfDirectReferral(referredUsers);
        for (User user : referredPairs) {
            System.out.print(user.getId() + ", ");
        }
    }
}
