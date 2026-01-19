package com.sboot.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.sboot.entity.User;
import com.sboot.security.JWTUtil;
import com.sboot.service.UserService;
import com.sboot.service.MailService.MailService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins ="http://localhost:4200")
@RestController
@RequestMapping("/auth")

//Authentication controllers

public class AuthenticationController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private MailService emailService;
    
    

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        Optional<User> optionalUser = userService.getByEmail(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        User user = optionalUser.get();

        // Check if account  locked (but allow ADMIN to always log in)
        if (user.getAccountLockedAt() != null 
            && (user.getRole() == null || !"ADMIN".equalsIgnoreCase(user.getRole().getRoleName()))) {
            return ResponseEntity.status(403).body(Map.of("error", "Account is locked. Please contact support."));
        }

        // Validate password using AuthenticationManager (to keep Spring Security in sync)
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {

            // Check if ADMIN
            if (user.getRole() != null && "ADMIN".equalsIgnoreCase(user.getRole().getRoleName())) {
                // For admin: ignore failed attempts, never lock
                return ResponseEntity.status(401).body(Map.of(
                    "error", "Invalid credentials."
                ));
            }

            // For other roles: increment failed attempts
            int failedAttempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(failedAttempts);

            if (failedAttempts >= 3) {
                user.setAccountLockedAt(LocalDateTime.now());
                
                
             // Send email notification
                emailService.sendSimpleMessage(
                    user.getUserEmail(),
                    "Account Locked",
                    "Dear " + user.getUserFullName() + ",\n\n" +
                    "Your account has been locked due to multiple failed login attempts.\n" +
                    "Please contact support to unlock your account.\n\n" +
                    "Thank you."
                );
            }

            userService.saveUser(user);

            int attemptsLeft = Math.max(0, 3 - failedAttempts);

            String message;
            if (failedAttempts >= 3) {
                message = "Account locked due to multiple failed attempts. Please contact support.";
            } else {
                message = "Invalid credentials. " + attemptsLeft + " attempt(s) remaining.";
            }

            return ResponseEntity.status(401).body(Map.of(
                "error", message,
                "failedAttempts", failedAttempts,
                "accountLocked", failedAttempts >= 3
            ));
        }

        // Successful login
        user.setFailedLoginAttempts(0);
        user.setLastLoginAt(LocalDateTime.now());
        user.setSessionExpiresAt(LocalDateTime.now().plusMinutes(10));
        userService.saveUser(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(username); 

        Map<String, Object> response = Map.of(
            "message", "Login successful",
            "username", user.getUserName() != null ? user.getUserName() : "",
            "userId", user.getUserId(),  		
            "sessionExpiresAt", user.getSessionExpiresAt() != null ? user.getSessionExpiresAt().toString() : "",
            "token", token,
            "role", user.getRole() != null ? user.getRole().getRoleName() : "USER",
            "mustReset", user.getMustReset() != null ? user.getMustReset() : false
        );

        return ResponseEntity.ok(response);
    }

    
    static class AuthRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
}

