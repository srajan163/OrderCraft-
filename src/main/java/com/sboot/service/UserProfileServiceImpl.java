package com.sboot.service;
 
import com.sboot.dto.AddressDTO;
import com.sboot.dto.UserProfileDTO;
import com.sboot.entity.Address;
import com.sboot.entity.User;
import com.sboot.repository.AddressRepository;
import com.sboot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
 
@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {
 
    private final UserRepository userRepo;
    private final AddressRepository addressRepo;
 
    // ✅ Save inside static/images folder
    private static final Path ROOT = Paths.get("src/main/resources/static/images");
 
    public UserProfileServiceImpl(UserRepository userRepo, AddressRepository addressRepo) {
        this.userRepo = userRepo;
        this.addressRepo = addressRepo;
    }
 
    // ===============================
    // GET PROFILE
    // ===============================
    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getProfile(String userId) {
        return userRepo.findById(userId)
                .map(this::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }
 
    // ===============================
    // UPDATE PROFILE (JSON fields)
    // ===============================
    @Override
    public UserProfileDTO updateProfile(String userId, UserProfileDTO dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
 
        updateUserFields(user, dto);
 
        if (dto.getAddress() != null) {
            Address address = updateOrCreateAddress(user.getAddress(), dto.getAddress());
            user.setAddress(addressRepo.save(address));
        }
 
        return mapToDto(userRepo.save(user));
    }
 
    // ===============================
    // UPDATE PROFILE IMAGE
    // ===============================
    @Override
    public UserProfileDTO updateProfileImage(String userId, MultipartFile file) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
 
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Uploaded file is empty");
            }
 
            if (!Files.exists(ROOT)) {
                Files.createDirectories(ROOT);
            }
 
            // Save file with unique name (userId + original filename)
            String filename = userId + "_" + file.getOriginalFilename();
            Path filePath = ROOT.resolve(filename);
 
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
 
            // Store relative path in DB
            user.setUserProfileImg("/images/" + filename);
 
            return mapToDto(userRepo.save(user));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile image", e);
        }
    }
 
    // ===============================
    // REMOVE PROFILE IMAGE
    // ===============================
    @Override
    public UserProfileDTO removeProfileImage(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
 
        user.setUserProfileImg(null); // reset to null in DB
        return mapToDto(userRepo.save(user));
    }
 
    // ===============================
    // HELPERS
    // ===============================
    private void updateUserFields(User user, UserProfileDTO dto) {
        user.setUserName(dto.getUserName());
        user.setUserFullName(dto.getUserFullName());
        user.setUserEmail(dto.getUserEmail());
        user.setUserMobile(dto.getUserMobile());
 
        if (dto.getUserProfileImg() != null) {
            user.setUserProfileImg(dto.getUserProfileImg());
        }
    }
 
    private Address updateOrCreateAddress(Address existing, AddressDTO adto) {
        Address address = (existing != null) ? existing : new Address();
        address.setAddressStreet(adto.getAddressStreet());
        address.setAddressCity(adto.getAddressCity());
        address.setAddressState(adto.getAddressState());
        address.setAddressPostalCode(adto.getAddressPostalCode());
        address.setAddressCountry(adto.getAddressCountry());
        return address;
    }
 
    private UserProfileDTO mapToDto(User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setUserFullName(user.getUserFullName());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserMobile(user.getUserMobile());
        dto.setUserProfileImg(user.getUserProfileImg());
        dto.setRoleName(user.getRole() != null ? user.getRole().getRoleName() : null);
 
        if (user.getAddress() != null) {
            dto.setAddress(mapToAddressDto(user.getAddress()));
        }
        return dto;
    }
 
    private AddressDTO mapToAddressDto(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setAddressId(address.getAddressId());
        dto.setAddressStreet(address.getAddressStreet());
        dto.setAddressCity(address.getAddressCity());
        dto.setAddressState(address.getAddressState());
        dto.setAddressPostalCode(address.getAddressPostalCode());
        dto.setAddressCountry(address.getAddressCountry());
        return dto;
    }
}
 
 