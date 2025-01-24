package com.astandro.library.service.impl;

import com.astandro.library.config.JwtTokenUtil;
import com.astandro.library.dto.LoginRequest;
import com.astandro.library.dto.UserRegistration;
import com.astandro.library.repository.RoleRepository;
import com.astandro.library.repository.UserRepository;
import com.astandro.library.repository.entity.Role;
import com.astandro.library.repository.entity.RoleName;
import com.astandro.library.repository.entity.User;
import com.astandro.library.service.AuthService;
import com.astandro.library.service.LoginAttemptService;
import com.astandro.library.service.MfaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor // Generates constructor for final fields
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final MfaService mfaService; // Ensure MfaServiceImpl is annotated with @Service
    private final RedisTemplate<String, String> redisTemplate; // Ensure RedisTemplate is configured in your app

    @Value("${otp.expiration.seconds:300}") // Default: 5 minutes if not configured in application.properties
    private int otpExpirationSeconds;

    @Autowired
    private final LoginAttemptService loginAttemptService;

    @Override
    public String authenticate(LoginRequest loginRequest) {
        String usernameOrEmail = loginRequest.getUsernameOrEmail();
        String password = loginRequest.getPassword();
        String otp = loginRequest.getOtp();

        if (loginAttemptService.isBlocked(usernameOrEmail)) {
            throw new RuntimeException("Account locked due to too many failed login attempts. Try again later.");
        }

        // Step 1: Find user by username or email
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // Step 2: Validate password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            loginAttemptService.loginFailed(usernameOrEmail);
            throw new RuntimeException("Invalid credentials");
        }

        // Step 3: If MFA is enabled, verify OTP
        if (user.getIsMfaEnabled()) {
            String redisKey = "otp:" + user.getEmail();

            // If OTP is not provided, generate and send one
            if (otp == null || otp.isEmpty()) {
                String generatedOtp = mfaService.generateOtp();
                redisTemplate.opsForValue().set(redisKey, generatedOtp, otpExpirationSeconds, TimeUnit.SECONDS);
                mfaService.sendOtpEmail(user.getEmail(), generatedOtp);
                loginAttemptService.loginFailed(usernameOrEmail);
                throw new RuntimeException("OTP sent to your email. Please provide the OTP to complete login.");
            }

            // Validate OTP
            String storedOtp = redisTemplate.opsForValue().get(redisKey);
            if (storedOtp == null || !storedOtp.equals(otp)) {
                loginAttemptService.loginFailed(usernameOrEmail);
                throw new RuntimeException("Invalid or expired OTP.");
            }

            // Remove OTP from Redis after successful validation
            redisTemplate.delete(redisKey);
        }

        // Step 4: Generate JWT token
        return jwtTokenUtil.generateToken(user.getUsername());
    }

    @Override
    public User registerUser(UserRegistration registration) {
        // Check if username or email already exists
        if (userRepository.existsByUsername((registration.getUsername()))) {
            throw new RuntimeException("Username is already taken");
        }

        if (userRepository.existsByEmail((registration.getEmail()))) {
            throw new RuntimeException("Email is already in use");
        }

        // Create new user
        User user = new User();
        user.setUsername(registration.getUsername());
        user.setEmail(registration.getEmail());
        user.setFullName(registration.getFullName());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));

        // Assign default role (VIEWER)
        Role viewerRole = roleRepository.findByName(RoleName.VIEWER)
                .orElseThrow(() -> new RuntimeException("Role not found!"));
        user.setRoles(Set.of(viewerRole));

        // Save user to the database
        return userRepository.save(user);
    }
}
