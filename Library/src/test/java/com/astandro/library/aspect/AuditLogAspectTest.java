package com.astandro.library.aspect;

import com.astandro.library.config.JwtTokenUtil;
import com.astandro.library.service.AuditService;
import com.astandro.library.util.LoggingUtil;
import org.aspectj.lang.Signature;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuditLogAspectTest {

    @Mock
    private AuditService auditService;

    @Mock
    private LoggingUtil loggingUtil;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Signature signature;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuditLogAspect auditLogAspect;

    @BeforeEach
    public void setUp() {
        // Set up necessary mocks
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock HttpServletRequest
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer mock_jwt_token");
        when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(mockRequest.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

        // Set the mocked request in RequestContextHolder
        ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attributes);

        // Mock JwtTokenUtil to return a username from the JWT token
        when(jwtTokenUtil.extractUsername(anyString())).thenReturn("testuser");

        // Mock JoinPoint and Signature
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("testAction");
    }

    @Test
    public void testLogAction() {
        // Act
        auditLogAspect.logAction(joinPoint);

        // Assert
        // Verify that the audit service's logAction method was called with the expected arguments
        verify(auditService).logAction("testuser", "testAction", "127.0.0.1", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36", "Desktop Device");

        // Verify that the logging utility's logAuditInfo method was called with the expected arguments
        verify(loggingUtil).logAuditInfo("testuser", "testAction", "127.0.0.1", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36", "Desktop Device");
    }
}
