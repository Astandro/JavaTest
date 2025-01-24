package com.astandro.library.aspect;

import com.astandro.library.config.JwtTokenUtil;
import com.astandro.library.service.AuditService;
import com.astandro.library.util.LoggingUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuditLogAspect {

    private final AuditService auditService;
    private final LoggingUtil loggingUtil;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuditLogAspect(AuditService auditService, LoggingUtil loggingUtil, JwtTokenUtil jwtTokenUtil) {
        this.auditService = auditService;
        this.loggingUtil = loggingUtil;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // Define the pointcut: Apply to all methods in the service layer
    @Pointcut("execution(* com.astandro.library.service..*(..)) && !execution(* com.astandro.library.service.AuditService.*(..))")
    public void serviceLayer() {}

    // After the method returns, log the action
    @AfterReturning("serviceLayer()")
    public void logAction(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the HTTP request from the RequestContextHolder
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            username = jwtTokenUtil.extractUsername(jwt);
        }

        // Get the action name (method name)
        String action = joinPoint.getSignature().getName();

        // Get IP address from the HTTP request
        String ip = request.getRemoteAddr();

        // Get the browser information from the HTTP request headers
        String browser = request.getHeader("User-Agent");

        // Get the device information from the User-Agent (simple example, can be extended)
        String device = extractDeviceFromUserAgent(browser);

        // Log the action in the Audit Service (save to database)
        auditService.logAction(username, action, ip, browser, device);

        // Log the audit information to the logs (real-time logging)
        loggingUtil.logAuditInfo(username, action, ip, browser, device);
    }

    // Helper method to extract device information from User-Agent string
    private String extractDeviceFromUserAgent(String userAgent) {
        if (userAgent == null) {
            return "Unknown Device";
        }

        // Basic check to detect mobile device
        if (userAgent.contains("Mobile")) {
            return "Mobile Device";
        } else if (userAgent.contains("Tablet")) {
            return "Tablet Device";
        } else {
            return "Desktop Device";
        }
    }
}
