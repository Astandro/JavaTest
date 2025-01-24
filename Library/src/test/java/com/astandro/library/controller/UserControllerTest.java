package com.astandro.library.controller;

import com.astandro.library.repository.entity.Article;
import com.astandro.library.repository.entity.Role;
import com.astandro.library.repository.entity.RoleName;
import com.astandro.library.repository.entity.User;
import com.astandro.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerTest {

    @Mock
    private UserService userService; // Mock the service layer

    @InjectMocks
    private UserController userController;// Inject mocks into the controller

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetUserById() throws Exception {
        // Mock the service call to return a sample article
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setEmail("email");
        Role role = new Role();
        role.setId(1L);
        role.setName(RoleName.valueOf("VIEWER"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        // Perform the GET request and check the response
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserByEmail() throws Exception {
        // Mock the service call to return a sample article
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setEmail("email");
        Role role = new Role();
        role.setId(1L);
        role.setName(RoleName.valueOf("VIEWER"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        when(userService.getUserByEmail("email")).thenReturn(Optional.of(user));

        // Perform the GET request and check the response
        mockMvc.perform(get("/api/users/email/email"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserByUsername() throws Exception {
        // Mock the service call to return a sample article
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setEmail("email");
        Role role = new Role();
        role.setId(1L);
        role.setName(RoleName.valueOf("VIEWER"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        when(userService.getUserByUsername("username")).thenReturn(Optional.of(user));

        // Perform the GET request and check the response
        mockMvc.perform(get("/api/users/username/username"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser() throws Exception {
        // Perform the GET request and check the response
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().is2xxSuccessful());
    }
}
