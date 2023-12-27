package com.eden.edenbe.restControllers;

import com.eden.edenbe.User;
import com.eden.edenbe.UserService;
import com.eden.edenbe.config.JwtUtils;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import static com.eden.edenbe.config.JwtUtils.generateToken;

//@CrossOrigin(origins = "*")
//@CrossOrigin(origins = {"http://localhost:3000", "http://13.212.101.199:80", "http://13.212.101.199"})
@RestController
@RequestMapping("/api/public")
public class LoginController {

    private final Authentication auth;

    @Autowired
    public LoginController(Authentication authentication) {
        this.auth = authentication;
    }

    @Autowired
    private UserService userService;

    /*
    * Authenticate the user based on the provided credentials (e.g., username and password).
    * If authentication is successful, generate a JWT token and return it.
    * If authentication fails, return an error response.
    */
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        try {
            boolean isUserValidx = auth.isUserValid(credentials.get("username"), credentials.get("password"));
            if (isUserValidx) {
                String token = generateToken(credentials.get("username"));
                Optional<User> user = userService.getUserByUsername(credentials.get("username"));

                Map<String, String> response = new HashMap<>();
                String userType = auth.getUserType(credentials.get("username"));
                String currentUserId = user.get().getId().toString();
                String currentUserPhoto = user.get().getProfile_picture_url();
                String currentUsername = user.get().getUsername();

                response.put("token", token);
                response.put("status", "200");
                response.put("role_id", userType);
                response.put("user_id", currentUserId);
                response.put("user_photo", currentUserPhoto);
                response.put("username", currentUsername);
                return ResponseEntity.ok(response);
            }
        } catch (JOSEException ex) {
            System.out.println("error jose " + ex);
        }
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid credentials");
        errorResponse.put("status", "401");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    };

    /*
    * Logout user
    * */
    @PostMapping(value = "/logout", produces = "application/json")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        String token = "";
        Map<String, String> response = new HashMap<>();
        String userType = "0";
        response.put("token", token);
        response.put("role_id", userType);
        response.put("status", "200");
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    };

    /*
     * Authenticate token:
     * */
    @GetMapping(value = "/validate", produces = "application/json")
    public ResponseEntity<Map<String, String>> validateToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        String username = JwtUtils.validateToken(token);
        if(username != null) {
            Optional<User> user = userService.getUserByUsername(username);

            Map<String, String> response = new HashMap<>();
            String userType = auth.getUserType(username);
            String currentUserId = user.get().getId().toString();
            String currentUserPhoto = user.get().getProfile_picture_url();

            response.put("token", token);
            response.put("status", "200");
            response.put("role_id", userType);
            response.put("user_id", currentUserId);
            response.put("user_photo", currentUserPhoto);
            response.put("username", username);

            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid token");
            errorResponse.put("status", "401");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
