package com.astandro.library.service;

public interface LoginAttemptService {
    void loginFailed(String username);
    boolean isBlocked(String username);
    void loginSucceeded(String username);
}
