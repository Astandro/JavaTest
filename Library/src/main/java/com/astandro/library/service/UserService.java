package com.astandro.library.service;

import com.astandro.library.repository.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);
    Object getUserByUsername(String username);
    Object getUserByEmail(String email);
    void deleteUser(Long id);
}
