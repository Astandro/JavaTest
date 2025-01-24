package com.astandro.library.service;

import com.astandro.library.dto.LoginRequest;
import com.astandro.library.dto.UserRegistration;
import com.astandro.library.repository.entity.User;

public interface AuthService {
    String authenticate(LoginRequest loginRequest);
    User registerUser(UserRegistration registration);
}