package com.astandro.library.controller;

import com.astandro.library.dto.LoginRequest;
import com.astandro.library.dto.UserRegistration;
import com.astandro.library.repository.UserRepository;
import com.astandro.library.repository.entity.User;
import com.astandro.library.service.AuthService;
import com.astandro.library.service.MfaService;
import com.astandro.library.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Operations related to Authorization")
public class AuthController {

    @Autowired
    private MfaService mfaService;

    @Autowired
    private UserRepository userRepository;

    private String generatedOtp;
    private final AuthService authService;

    @Autowired
    private UserService userService;

    public AuthController(AuthService authService, MfaService mfaService, UserService userService, UserRepository userRepository) {
        this.authService = authService;
        this.mfaService = mfaService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Cacheable(value = "users", key = "#loginRequest.usernameOrEmail")
    @PostMapping("/login")
    @Operation(summary = "User login")
    public String login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Fetching user login from database for " + loginRequest.getUsernameOrEmail());
        return authService.authenticate(loginRequest);
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistration registration) {
        User registeredUser = authService.registerUser(registration);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    // Endpoint to request OTP
    @PostMapping("/request-otp")
    @Operation(summary = "Request otp for MFA")
    public ResponseEntity<String> requestOtp(@RequestParam String email) {
        Optional<User> userOpt = (Optional<User>) userService.getUserByEmail(email);
        if (userOpt.isPresent()) {
            // Generate OTP and send it
            generatedOtp = mfaService.generateOtp();
            mfaService.sendOtpEmail(email, generatedOtp);
            return ResponseEntity.ok("OTP sent to your email.");
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    // Endpoint to verify OTP
    @PostMapping("/verify-otp")
    @Operation(summary = "Verify otp for MFA")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        Optional<User> userOpt = (Optional<User>) userService.getUserByEmail(email);
        if (userOpt.isPresent()) {
            // Verify OTP
            if (generatedOtp.equals(otp)) {
                // Mark MFA as enabled for the user
                User user = userOpt.get();
                user.setMfaEnabled();
                userRepository.save(user);
                return ResponseEntity.ok("MFA enabled successfully.");
            } else {
                return ResponseEntity.status(400).body("Invalid OTP.");
            }
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    public void setGeneratedOtp(String generatedOtp) {
        this.generatedOtp = generatedOtp;
    }
}
