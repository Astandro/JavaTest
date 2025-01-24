package com.astandro.library.service;

public interface MfaService {
    String generateOtp();
    void sendOtpEmail(String email, String otp);

    // Generate and send OTP while storing it in Redis
    void generateAndSendOtp(Long userId, String email);

    // Validate OTP
    boolean validateOtp(Long userId, String otp);
}
