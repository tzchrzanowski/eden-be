package com.eden.edenbe.restControllers;
import com.eden.edenbe.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class Authentication {
//    @Value("${spring.datasource.url}")
//    private String dbUrl;
//    @Value("${spring.datasource.username}")
//    private String dbUsername;
//    @Value("${spring.datasource.password}")
//    private String dbPassword;

    private String dbUrl = "jdbc:mysql://localhost:3306/eden_db";
    private String dbUsername = "root";
    private String dbPassword = "12Bomb21!@";

    private final UserService userService;

    @Autowired
    public Authentication(UserService userService) {
        this.userService = userService;
    }

    /*
    * Takes provided credentials, and checks if database contains this user
    * */
    public boolean isUserValid(String username, String password) {
        System.out.println("tries to authenticate user: " + username + " ,password: " + password);

        try {
            Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String query = "SELECT * FROM Users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                String storedHashedPassword = resultSet.getString("password");

                boolean isValid = userService.isPasswordValid(password, storedHashedPassword);

                resultSet.close();
                statement.close();
                connection.close();

                return isValid;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false; // default return statement;
    }
}
