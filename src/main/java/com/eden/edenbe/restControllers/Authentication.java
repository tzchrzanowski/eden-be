package com.eden.edenbe.restControllers;
import com.eden.edenbe.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class Authentication {
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
            String query = "SELECT * FROM users WHERE username = ?";
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

    public String getUserType(String username) {
        try {
            Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String query = "SELECT role_id FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userType = resultSet.getInt("role_id");
                resultSet.close();
                statement.close();
                connection.close();
                return "" + userType;
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "0";
    }
}
