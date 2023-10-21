package com.eden.edenbe.restControllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.eden.edenbe.config.JwtUtils.generateToken;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/public")
public class LoginController {

//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public static class UnauthorizedException extends RuntimeException {
//        public UnauthorizedException(String message) {
//            super(message);
//        }
//    }
    /*
    * Authenticate the user based on the provided credentials (e.g., username and password).
    * If authentication is successful, generate a JWT token and return it.
    * If authentication fails, return an error response.
    */

//    @PostMapping("/login")
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        System.out.println("entered login logic..." +  credentials.get("username") + " " + credentials.get("password"));
        if ("admin".equals(credentials.get("username")) && "password".equals(credentials.get("password"))){
            System.out.println("credentials ok.. in if");
            String token = "temp-token0101";
//            String token = generateToken(credentials.get("username"));
            System.out.println("token = " + token);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else{
            System.out.println("credentials WRONG.. in else");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid credentials");
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("error", "Invalid credentials");
//            return errorResponse;
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    };


}
