package com.sboot.controller;

import com.sboot.dto.UserProfileDTO;
import com.sboot.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }


    // ✅ Get user profile
    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDTO> getProfile(@PathVariable String userId) {
        return ResponseEntity.ok(userProfileService.getProfile(userId));
    }

    // ✅ Update user profile details (without image)
    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDTO> updateProfile(
            @PathVariable String userId,
            @RequestBody UserProfileDTO dto) {
        return ResponseEntity.ok(userProfileService.updateProfile(userId, dto));
    }

    // ✅ Upload new profile image
    @PostMapping("/{userId}/profile/image")
    public ResponseEntity<UserProfileDTO> uploadProfileImage(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userProfileService.updateProfileImage(userId, file));
    }

    // ✅ Remove profile image (reset to null or default)
    @DeleteMapping("/{userId}/profile/image")
    public ResponseEntity<UserProfileDTO> removeProfileImage(@PathVariable String userId) {
        return ResponseEntity.ok(userProfileService.removeProfileImage(userId));
    }
}

