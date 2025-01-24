package com.astandro.library.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingUtil {

    private static final Logger logger = LoggerFactory.getLogger(LoggingUtil.class);

    // Log audit information
    public void logAuditInfo(String username, String action, String ip, String browser, String device) {
        logger.info("Audit Log - Username: {}, Action: {}, IP: {}, Browser: {}, Device: {}",
                username, action, ip, browser, device);
    }
}
