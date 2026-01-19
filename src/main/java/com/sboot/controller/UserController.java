package com.sboot.controller;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.sboot.dto.UserDto;
import com.sboot.entity.Role;
import com.sboot.entity.User;
import com.sboot.repository.RoleRepository;
import com.sboot.repository.UserRepository;
import com.sboot.service.SecuredPasswordGenerator;
import com.sboot.service.UserService;
import com.sboot.service.MailService.MailService;
import com.sboot.util.OtpStorage;
import com.sboot.util.OtpUtil;

@CrossOrigin(origins ="http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private MailService emailService;
    
    @Autowired
    private OtpUtil otpUtil;
    
    @Autowired
    private OtpStorage otpStorage;
    
    @Autowired
    private RoleRepository roleRepo;
    
    @Autowired
    private UserRepository userRepository;
    
    
    
   
    
    

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }
    
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
 

    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }


    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUserById(id);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
    
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request, Principal principal) {
        String username = principal.getName();

        System.out.println("Authenticated principal name: " + username);

        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            // Try email lookup too
            userOpt = userService.getByEmail(username);
        }

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = userOpt.get();

        // Compare the current password provided with the stored hash
        boolean matches = passwordEncoder.matches(request.getCurrentPassword(), user.getUserPassword());
        if (!matches) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Current password is incorrect.");
        }
        
      //  User user = userOpt.orElseThrow(() -> new RuntimeException("User not found"));
        
//        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getUserPassword())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Current password is incorrect.");
//        }

        user.setUserPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setMustReset(false);
        userService.saveUser(user);

        return ResponseEntity.ok("Password reset successfully.");
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        emailService.generateAndStoreOtp(email);
        return ResponseEntity.ok("OTP sent to email.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }
        if (otp == null || otp.isBlank()) {
            return ResponseEntity.badRequest().body("OTP is required.");
        }

        boolean isValid = emailService.verifyOtp(email, otp);

        if (isValid) {
            return ResponseEntity.ok("OTP verified.");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }


    @PostMapping("/reset-password-with-otp")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required."));
        }
        if (otp == null || otp.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "OTP is required."));
        }
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "New password is required."));
        }

        // Check if user exists
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "User with this email not found."));
        }

        User user = opt.get();

        // Disallow resetting ADMIN passwords via this endpoint
        if ("ADMIN".equalsIgnoreCase(user.getRole().getRoleName())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Admin password cannot be reset via this method."));
        }

        // Validate OTP
        if (!emailService.verifyOtp(email, otp)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid OTP."));
        }

        // Check if the new password is the same as the old one
        if (passwordEncoder.matches(newPassword, user.getUserPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "New password must be different from the old one."));
        }

        // Save the new password
        userService.updatePassword(email, newPassword);

        return ResponseEntity.ok(Map.of("message", "Password reset successfully."));
    }


    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        Optional<User> optionalUser = userService.getByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No user found with that email.");
        }

        User user = optionalUser.get();

        // Generate temporary random password
        String tempPassword =  SecuredPasswordGenerator.generatePassword();

        
       
        
        // Encode and save it
        user.setUserPassword(passwordEncoder.encode(tempPassword));
        user.setMustReset(true);
        userService.saveUser(user);

        // TODO: send email
        emailService.sendEmail(
            user.getUserEmail(),
            "Password Reset Request",
            "Your temporary password is: " + tempPassword + "\nPlease log in and reset your password."
        );

        return ResponseEntity.ok("Temporary password sent to your email.");
    }



    
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleRepo.findAll();
        return ResponseEntity.ok(roles);
    }
    
    
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String query) {
        Optional<User> users = userService.searchByEmailOrName(query);
        List<UserDto> result = users.stream().map(UserDto::fromEntity).toList();
        return ResponseEntity.ok(result);
    }

    
    @GetMapping("/locked-users")
    public ResponseEntity<List<User>> getLockedUsers() {
        List<User> lockedUsers = userRepository.findByAccountLockedAtIsNotNull();
 
        if (lockedUsers.isEmpty()) {
            return ResponseEntity.ok(List.of()); // return empty JSON array
        }
 
        return ResponseEntity.ok(lockedUsers);
    }
    
    @PutMapping("/unlock-account")
    public ResponseEntity<?> unlockAccount(@RequestParam String email) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found."));
        }
        User user = opt.get();
        
        if (user.getAccountLockedAt() == null) {
            return ResponseEntity.ok("Account is not locked.");
        }

        user.setFailedLoginAttempts(0);
        user.setAccountLockedAt(null);
        userRepository.save(user);
        
        
        // Send email notification
        emailService.sendSimpleMessage(
            user.getUserEmail(),
            "Account Unlocked",
            "Dear " + user.getUserFullName() + ",\n\n" +
            "Your account has been unlocked by the administrator. You can now log in again.\n\n" +
            "Thank you."
        );
        return ResponseEntity.ok(Map.of("message", "Account unlocked."));
    }


  
    public static class ResetPasswordRequest {
    	
    	
    	
    	private String currentPassword;
    	
    	
    	private String newPassword;
    	
    	
        public String getCurrentPassword() {
			return currentPassword;
		}
		public void setCurrentPassword(String currentPassword) {
			this.currentPassword = currentPassword;
		}
		
        // Getter & Setter
        public String getNewPassword() {
            return newPassword;
        }
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

}
