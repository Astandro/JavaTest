package com.astandro.library.service;

public interface AuditService {
    void logAction(String username, String action, String ip, String browser, String device);
}
