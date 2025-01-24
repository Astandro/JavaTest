package com.astandro.library.service;

import com.astandro.library.service.MfaService;
import com.astandro.library.service.impl.MfaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MfaServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private MfaServiceImpl mfaService;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateOtp() {
        String otp = mfaService.generateOtp();

        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d{6}"), "OTP should only contain digits.");
    }

    @Test
    void testSendOtpEmail() {
        String email = "user@example.com";
        String otp = "123456";

        mfaService.sendOtpEmail(email, otp);

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setFrom("astandro.tc@gmail.com");
        expectedMessage.setTo(email);
        expectedMessage.setSubject("Your One-Time Password (OTP)");
        expectedMessage.setText("Your OTP is: " + otp + "\n\nIt will expire in 5 minutes.");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testGenerateAndSendOtp() {
        Long userId = 1L;
        String otp = "123456";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        mfaService.generateAndSendOtp(userId, otp);
    }

    @Test
    void testValidateOtp() {
        Long userId = 1L;
        String otp = "123456";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        mfaService.validateOtp(userId, otp);
    }
}
