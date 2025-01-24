package com.astandro.library.service.impl;

import com.astandro.library.service.MfaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class MfaServiceImpl implements MfaService {

    @Autowired
    private final JavaMailSender mailSender;

    @Autowired
    private final RedisTemplate<String, String> redisTemplate;

    private static final int OTP_LENGTH = 6;
    private static final String OTP_PREFIX = "OTP_"; // Key prefix for Redis storage
    private static final long OTP_EXPIRATION = 5 * 60; // 5 minutes in seconds

    // Generate a random OTP
    @Override
    public String generateOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    // Send OTP via email
    @Override
    public void sendOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("astandro.tc@gmail.com");  // Use the same email you configured
        message.setTo(email);
        message.setSubject("Your One-Time Password (OTP)");
        message.setText("Your OTP is: " + otp + "\n\nIt will expire in 5 minutes.");
        mailSender.send(message);
    }

    // Generate and send OTP while storing it in Redis
    @Override
    public void generateAndSendOtp(Long userId, String email) {
        // Generate OTP
        String otp = generateOtp();

        // Store OTP in Redis with expiration
        String redisKey = OTP_PREFIX + userId;
        redisTemplate.opsForValue().set(redisKey, otp, OTP_EXPIRATION, TimeUnit.SECONDS);

        // Send OTP via email
        sendOtpEmail(email, otp);
    }

    // Validate OTP
    @Override
    public boolean validateOtp(Long userId, String otp) {
        String redisKey = OTP_PREFIX + userId;
        String storedOtp = redisTemplate.opsForValue().get(redisKey);

        // Return true if OTP matches, false otherwise
        return storedOtp != null && storedOtp.equals(otp);
    }
}
