package com.astandro.library.service;

import com.astandro.library.service.LoginAttemptService;
import com.astandro.library.service.impl.LoginAttemptServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

class LoginAttemptServiceImplTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private LoginAttemptServiceImpl loginAttemptService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testLoginFailed_FirstAttempt() {
        String username = "testUser";
        String key = "login:attempts:" + username;

        // Mock behavior for incrementing attempts
        when(valueOperations.increment(key)).thenReturn(1L);

        // Act
        loginAttemptService.loginFailed(username);

        // Assert that the expiration is set for the first attempt
        verify(redisTemplate, times(1)).expire(key, 30, TimeUnit.MINUTES);
    }

    @Test
    void testLoginFailed_SubsequentAttempt() {
        String username = "testUser";
        String key = "login:attempts:" + username;

        // Mock behavior for incrementing attempts
        when(valueOperations.increment(key)).thenReturn(2L);

        // Act
        loginAttemptService.loginFailed(username);

        // Assert that expiration is not reset for subsequent attempts
        verify(redisTemplate, never()).expire(anyString(), anyLong(), any());
    }

    @Test
    void testIsBlocked_UserBlocked() {
        String username = "testUser";
        String key = "login:attempts:" + username;

        // Mock behavior to simulate maximum attempts reached
        when(valueOperations.get(key)).thenReturn(String.valueOf(5));

        // Act
        boolean isBlocked = loginAttemptService.isBlocked(username);

        // Assert
        verify(valueOperations, times(1)).get(key);
        assert isBlocked;
    }

    @Test
    void testIsBlocked_UserNotBlocked() {
        String username = "testUser";
        String key = "login:attempts:" + username;

        // Mock behavior to simulate no attempts
        when(valueOperations.get(key)).thenReturn(null);

        // Act
        boolean isBlocked = loginAttemptService.isBlocked(username);

        // Assert
        verify(valueOperations, times(1)).get(key);
        assert !isBlocked;
    }

    @Test
    void testLoginSucceeded() {
        String username = "testUser";
        String key = "login:attempts:" + username;

        // Act
        loginAttemptService.loginSucceeded(username);

        // Assert that the key is deleted from Redis
        verify(redisTemplate, times(1)).delete(key);
    }
}
