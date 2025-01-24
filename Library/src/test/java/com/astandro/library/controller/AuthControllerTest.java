package com.astandro.library.controller;

import com.astandro.library.dto.LoginRequest;
import com.astandro.library.dto.UserRegistration;
import com.astandro.library.repository.UserRepository;
import com.astandro.library.repository.entity.Article;
import com.astandro.library.repository.entity.User;
import com.astandro.library.service.ArticleService;
import com.astandro.library.service.AuthService;
import com.astandro.library.service.MfaService;
import com.astandro.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@RunWith(MockitoJUnitRunner.class)
class AuthControllerTest {

    @Mock
    private AuthService authService; // Mock the service layer

    @InjectMocks
    private AuthController authController;// Inject mocks into the controller

    @Mock
    private MfaService mfaService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLogin() throws Exception {
        // Mock the service call to return a sample article
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("test@test.com");
        loginRequest.setPassword("password");

        when(authService.authenticate(any())).thenReturn("dummyJWT");

        // Perform the GET request and check the response
        String requestBody = """
                {
                    "username": "test@test.com",
                    "password": "password"
                }
                """;
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON) // This is required
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void testRegister() throws Exception {
        // Mock the service call to return a sample article
        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setUsername("test@test.com");
        userRegistration.setPassword("password");
        userRegistration.setEmail("test@test.com");
        userRegistration.setFullName("Test");

        when(authService.registerUser(any())).thenReturn(null);

        // Perform the GET request and check the response
        String requestBody = """
                {
                    "username": "test@test.com",
                    "password": "password",
                    "email": "test@test.com",
                    "fullName": "Test"
                }
                """;
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON) // This is required
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void testRequestOtp() throws Exception {
        // Mock the service call to return a sample article
        String email = "test@test.com";

        when(userService.getUserByEmail(email)).thenReturn(Optional.of(new User()));
        when(mfaService.generateOtp()).thenReturn(null);
        mfaService.sendOtpEmail(email,"123");

        mockMvc.perform(post("/api/auth/request-otp?email=test@test.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testVerifyOtp() throws Exception {
        // Mock the service call to return a sample article
        String email = "test@test.com";
        authController.setGeneratedOtp("123");

        when(userService.getUserByEmail(email)).thenReturn(Optional.of(new User()));

        mockMvc.perform(post("/api/auth/verify-otp?email=test@test.com&otp=123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
