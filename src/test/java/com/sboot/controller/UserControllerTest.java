package com.sboot.controller;

import com.sboot.controller.UserController;
import com.sboot.entity.User;
import com.sboot.repository.RoleRepository;
import com.sboot.repository.UserRepository;
import com.sboot.service.MailService.MailService;
import com.sboot.service.UserService;
import com.sboot.util.OtpStorage;
import com.sboot.util.OtpUtil;
 
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 
import java.security.Principal;
import java.util.*;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
class UserControllerTest {
 
    @InjectMocks
    private UserController controller;
 
    @Mock private UserService userService;
    @Mock private BCryptPasswordEncoder encoder;
    @Mock private MailService emailService;
    @Mock private OtpUtil otpUtil;
    @Mock private OtpStorage otpStorage;
    @Mock private RoleRepository roleRepo;
    @Mock private UserRepository userRepository;
 
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    void testRegisterUser() {
        User user = new User();
        when(userService.registerUser(user)).thenReturn(user);
 
        User result = controller.registerUser(user);
        assertEquals(user, result);
    }
 
    @Test
    void testGetAllUsers() {
        List<User> users = List.of(new User());
        when(userService.getAllUsers()).thenReturn(users);
 
        List<User> result = controller.getAllUsers();
        assertEquals(users.size(), result.size());
    }
 
    @Test
    void testResetPassword_Success() {
        Principal mockPrincipal = () -> "testUser";
        User user = new User();
        user.setUserPassword("old");
 
        UserController.ResetPasswordRequest request = new UserController.ResetPasswordRequest();
        request.setCurrentPassword("old");
        request.setNewPassword("new");
 
        when(userService.getUserByUsername("testUser")).thenReturn(Optional.of(user));
        when(encoder.matches("old", "old")).thenReturn(true);
 
        ResponseEntity<?> response = controller.resetPassword(request, mockPrincipal);
 
        assertEquals(200, response.getStatusCodeValue());
    }
}
 
 