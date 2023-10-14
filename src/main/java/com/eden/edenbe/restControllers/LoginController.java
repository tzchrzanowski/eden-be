package com.eden.edenbe.restControllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
public class LoginController {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
    /*
    * Authenticate the user based on the provided credentials (e.g., username and password).
    * If authentication is successful, generate a JWT token and return it.
    * If authentication fails, return an error response.
    */
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) {
        if ("admin".equals(credentials.get("username")) && "password".equals(credentials.get("password"))){
            String token = "temp-token0101";
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            System.out.println("credentials ok.. in if");
            return response;
        } else{
            System.out.println("credentials WRONG.. in else");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid credentials");
            return errorResponse;
        }
    };
}
