package com.astandro.library.service;

import com.astandro.library.repository.AuditLogRepository;
import com.astandro.library.repository.entity.AuditLog;
import com.astandro.library.service.AuditService;
import com.astandro.library.service.impl.AuditServiceImpl;
import com.astandro.library.util.LoggingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class AuditServiceImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private LoggingUtil loggingUtil;

    @InjectMocks
    private AuditServiceImpl auditService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogAction() {
        String username = "testUser";
        String action = "TEST_ACTION";
        String ip = "127.0.0.1";
        String browser = "Chrome";
        String device = "PC";

        // Act
        auditService.logAction(username, action, ip, browser, device);

        // Assert that the audit log is saved to the repository
        verify(auditLogRepository, times(1)).save(argThat(log ->
                log.getUsername().equals(username) &&
                        log.getAction().equals(action) &&
                        log.getIp().equals(ip) &&
                        log.getBrowser().equals(browser) &&
                        log.getDevice().equals(device) &&
                        log.getTimestamp() != null // Ensure timestamp is set
        ));

        // Assert that LoggingUtil logs the action
        verify(loggingUtil, times(1)).logAuditInfo(username, action, ip, browser, device);
    }
}
