package com.astandro.library.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilTest {

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private Claims claims;

    private String testUsername;
    private String testToken;

    @BeforeEach
    public void setUp() {
        testUsername = "testuser";
        // Generate a valid token for testing
        testToken = jwtTokenUtil.generateToken(testUsername);
    }

    @Test
    public void testExtractUsername() {
        // Mock extracting the username from the token
        String username = jwtTokenUtil.extractUsername(testToken);

        // Verify if the username extracted is correct
        assertEquals(testUsername, username);
    }

    @Test
    public void testGenerateToken() {
        // Test if the generated token contains the username
        String token = jwtTokenUtil.generateToken(testUsername);

        String usernameFromToken = jwtTokenUtil.extractUsername(token);

        // Verify if the token contains the expected username
        assertEquals(testUsername, usernameFromToken);
    }

    @Test
    public void testValidateToken() {
        // Test if the token is valid for the correct username
        boolean isValid = jwtTokenUtil.validateToken(testToken, testUsername);

        // Token should be valid as username matches and it is not expired
        assertTrue(isValid);
    }

    @Test
    public void testTokenExpiration() {
        // Mock the expiration date method to simulate token expiration
        Date expirationDate = new Date(System.currentTimeMillis() - 1000); // 1 second in the past

        // Validate the token with an expired expiration date
        boolean isExpired = jwtTokenUtil.isTokenExpired(testToken);
    }
}
