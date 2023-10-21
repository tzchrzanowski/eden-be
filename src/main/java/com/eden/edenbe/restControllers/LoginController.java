package com.eden.edenbe.restControllers;

import com.nimbusds.jose.JOSEException;
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

    /*
    * Authenticate the user based on the provided credentials (e.g., username and password).
    * If authentication is successful, generate a JWT token and return it.
    * If authentication fails, return an error response.
    */
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        try {
            if ("admin".equals(credentials.get("username")) && "password".equals(credentials.get("password"))){
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
