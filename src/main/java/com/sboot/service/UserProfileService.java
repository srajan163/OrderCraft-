package com.sboot.service;

 
import org.springframework.web.multipart.MultipartFile;
import com.sboot.dto.UserProfileDTO;
 


import org.springframework.web.multipart.MultipartFile;
import com.sboot.dto.UserProfileDTO;

public interface UserProfileService {
    UserProfileDTO getProfile(String userId);
    UserProfileDTO updateProfile(String userId, UserProfileDTO dto);
    UserProfileDTO updateProfileImage(String userId, MultipartFile file);
    UserProfileDTO removeProfileImage(String userId);
}

