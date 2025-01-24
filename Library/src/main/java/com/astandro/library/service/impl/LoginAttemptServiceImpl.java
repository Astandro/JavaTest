package com.astandro.library.service.impl;

import com.astandro.library.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 30; // in minutes

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void loginFailed(String username) {
        String key = getKey(username);
        Long attempts = redisTemplate.opsForValue().increment(key);
        if (attempts == 1) {
            // Set expiration if this is the first failed attempt
            redisTemplate.expire(key, LOCK_TIME_DURATION, TimeUnit.MINUTES);
        }
    }

    public boolean isBlocked(String username) {
        String key = getKey(username);
        String attempts = redisTemplate.opsForValue().get(key);
        if (attempts != null && Integer.parseInt(attempts) >= MAX_ATTEMPTS) {
            return true;
        }
        return false;
    }

    public void loginSucceeded(String username) {
        String key = getKey(username);
        redisTemplate.delete(key);
    }

    private String getKey(String username) {
        return "login:attempts:" + username;
    }
}
