package com.astandro.library.entity;

import com.astandro.library.repository.entity.Role;
import com.astandro.library.repository.entity.RoleName;
import com.astandro.library.repository.entity.User;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void testUserEntity() {
        User user = new User();

        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("admin");
        user.setEmail("admin@admin.com");
        user.setMfaEnabled();
        user.setFullName("admin");
        Role role = new Role();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        role.setName(RoleName.valueOf("VIEWER"));
        user.setRoles(roles);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("admin", user.getUsername());
        assertEquals("admin", user.getPassword());
        assertEquals("admin@admin.com", user.getEmail());
        assertEquals("admin", user.getFullName());
        assertEquals(RoleName.VIEWER, role.getName());
    }

    @Test
    void testUserConstructor() {
        User user = new User("admin","admin@admin.com","admin","admin",true);

        user.setId(1L);
        user.getAuthorities();
        Role role = new Role();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        role.setName(RoleName.valueOf("VIEWER"));
        user.setRoles(roles);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("admin", user.getUsername());
        assertEquals("admin", user.getPassword());
        assertEquals("admin@admin.com", user.getEmail());
        assertEquals("admin", user.getFullName());
        assertEquals(true, user.getIsMfaEnabled());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
        assertEquals(RoleName.VIEWER, role.getName());
    }
}
