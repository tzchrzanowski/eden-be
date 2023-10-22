package com.eden.edenbe.restControllers;

import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import static com.eden.edenbe.config.JwtUtils.generateToken;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/public")
public class LoginController {

    private final Authentication auth;

    @Autowired
    public LoginController(Authentication authentication) {
        this.auth = authentication;
    }

    /*
    * Authenticate the user based on the provided credentials (e.g., username and password).
    * If authentication is successful, generate a JWT token and return it.
    * If authentication fails, return an error response.
    */
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        try {
            boolean isUserValidx = auth.isUserValid(credentials.get("username"), credentials.get("password"));
            System.out.println("Is user valid using auth: " + isUserValidx);

            if (isUserValidx) {
                String token = generateToken(credentials.get("username"));
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            }
        } catch (JOSEException ex) {
            System.out.println("error jose " + ex);
        }
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    };
}
