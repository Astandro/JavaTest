package com.astandro.library.service;

import com.astandro.library.config.JwtTokenUtil;
import com.astandro.library.dto.LoginRequest;
import com.astandro.library.dto.UserRegistration;
import com.astandro.library.repository.RoleRepository;
import com.astandro.library.repository.UserRepository;
import com.astandro.library.repository.entity.Role;
import com.astandro.library.repository.entity.RoleName;
import com.astandro.library.repository.entity.User;
import com.astandro.library.service.LoginAttemptService;
import com.astandro.library.service.MfaService;
import com.astandro.library.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private MfaService mfaService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private LoginAttemptService loginAttemptService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_mfaEnabledWithoutOtp_sendsOtpAndThrowsException() {
        String username = "testuser";
        String password = "password";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setMfaEnabled();
        user.setEmail("test@example.com");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail(username);
        loginRequest.setPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        Exception exception = assertThrows(RuntimeException.class, () -> authService.authenticate(loginRequest));
        assertEquals("OTP sent to your email. Please provide the OTP to complete login.", exception.getMessage());
        verify(mfaService).sendOtpEmail(eq(user.getEmail()), any());
    }

    @Test
    void registerUser_validRegistration_savesUser() {
        UserRegistration registration = new UserRegistration();
        registration.setUsername("testuser");
        registration.setEmail("test@example.com");
        registration.setPassword("password");

        Role viewerRole = new Role();
        viewerRole.setName(RoleName.VIEWER);

        when(userRepository.existsByUsername(registration.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registration.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleName.VIEWER)).thenReturn(Optional.of(viewerRole));
        when(passwordEncoder.encode(registration.getPassword())).thenReturn("encoded-password");

        authService.registerUser(registration);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_existingUsername_throwsException() {
        UserRegistration registration = new UserRegistration();
        registration.setUsername("testuser");
        registration.setEmail("test@example.com");

        when(userRepository.existsByUsername(registration.getUsername())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> authService.registerUser(registration));
        assertEquals("Username is already taken", exception.getMessage());
    }

    @Test
    void registerUser_existingEmail_throwsException() {
        UserRegistration registration = new UserRegistration();
        registration.setUsername("testuser");
        registration.setEmail("test@example.com");

        when(userRepository.existsByEmail(registration.getEmail())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> authService.registerUser(registration));
        assertEquals("Email is already in use", exception.getMessage());
    }

    @Test
    void authenticateBlocked_throwsException() {
        String username = "testuser";
        String password = "password";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setMfaEnabled();
        user.setEmail("test@example.com");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail(username);
        loginRequest.setPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        when(loginAttemptService.isBlocked(any())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> authService.authenticate(loginRequest));
        assertEquals("Account locked due to too many failed login attempts. Try again later.", exception.getMessage());
    }

    @Test
    void authenticateWrongPassword_throwsException() {
        String username = "testuser";
        String password = "password";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setMfaEnabled();
        user.setEmail("test@example.com");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail(username);
        loginRequest.setPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        when(loginAttemptService.isBlocked(any())).thenReturn(false);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> authService.authenticate(loginRequest));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void authenticatevalidateOTP_throwsException() {
        String username = "testuser";
        String password = "password";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setMfaEnabled();
        user.setEmail("test@example.com");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail(username);
        loginRequest.setPassword(password);
        loginRequest.setOtp("123");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        when(loginAttemptService.isBlocked(any())).thenReturn(false);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> authService.authenticate(loginRequest));
        assertEquals("Invalid or expired OTP.", exception.getMessage());
    }
}
