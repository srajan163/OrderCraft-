//package com.boot.ordercraft.controller;
//
//import com.boot.ordercraft.dao.EmailRequest;
//import com.boot.ordercraft.dao.PasswordResetRequest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class AuthenticationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void testCheckEmail_ValidEmail_ReturnsOk() throws Exception {
//        EmailRequest request = new EmailRequest("po1@example.com"); // replace with actual registered email
//
//        mockMvc.perform(post("/auth/check-email")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Email is valid."));
//    }
//
//    @Test
//    public void testCheckEmail_InvalidEmail_ReturnsBadRequest() throws Exception {
//        EmailRequest request = new EmailRequest("invalid@example.com");
//
//        mockMvc.perform(post("/auth/check-email")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error").value("Email not found."));
//    }
//
//    @Test
//    public void testForgotPassword_SuccessfulReset() throws Exception {
//        PasswordResetRequest request = new PasswordResetRequest("testuser@example.com", "NewSecurePass@123"); // registered non-admin email
//
//        mockMvc.perform(post("/auth/forgot-password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Password updated successfully."));
//    }
//
//    @Test
//    public void testForgotPassword_EmailNotFound() throws Exception {
//        PasswordResetRequest request = new PasswordResetRequest("notfound@example.com", "AnyPassword");
//
//        mockMvc.perform(post("/auth/forgot-password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error").value("User with this email not found."));
//    }
//
//    @Test
//    public void testForgotPassword_SameAsOldPassword() throws Exception {
//        PasswordResetRequest request = new PasswordResetRequest("testuser@example.com", "P@123"); // replace with current password
//
//        mockMvc.perform(post("/auth/forgot-password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error").value("New password must be different from the old one."));
//    }
//}
