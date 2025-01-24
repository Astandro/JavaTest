package com.astandro.library.service.impl;

import com.astandro.library.repository.AuditLogRepository;
import com.astandro.library.repository.entity.AuditLog;
import com.astandro.library.service.AuditService;
import com.astandro.library.util.LoggingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;
    private final LoggingUtil loggingUtil;

    @Autowired
    public AuditServiceImpl(AuditLogRepository auditLogRepository, LoggingUtil loggingUtil) {
        this.auditLogRepository = auditLogRepository;
        this.loggingUtil = loggingUtil;
    }

    @Override
    public void logAction(String username, String action, String ip, String browser, String device) {
        // Save audit log to the database
        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setIp(ip);
        auditLog.setBrowser(browser);
        auditLog.setDevice(device);
        auditLog.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(auditLog);

        // Log audit info using LoggingUtil
        loggingUtil.logAuditInfo(username, action, ip, browser, device);
    }
}
