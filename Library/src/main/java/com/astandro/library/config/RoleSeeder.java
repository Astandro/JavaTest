package com.astandro.library.config;

import com.astandro.library.repository.RoleRepository;
import com.astandro.library.repository.entity.Role;
import com.astandro.library.repository.entity.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class RoleSeeder {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void seedRoles() {
        if (roleRepository.count() == 0) {
            for (RoleName roleName : RoleName.values()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
            System.out.println("Roles seeded successfully!");
        }
    }
}
