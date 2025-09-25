package com.example.userservice.config;

import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserRole;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner seedData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            for (UserRole roleName : UserRole.values()) {
                roleRepository.findByName(roleName).orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    log.info("Creating role {}", roleName);
                    return roleRepository.save(role);
                });
            }

            userRepository.findByEmail("admin@ecommerce.local").orElseGet(() -> {
                User admin = new User();
                admin.setEmail("admin@ecommerce.local");
                admin.setPassword(passwordEncoder.encode("ChangeMe123!"));
                admin.setFirstName("System");
                admin.setLastName("Administrator");
                admin.setPhoneNumber("0000000000");
                Set<Role> adminRoles = EnumSet.of(UserRole.ADMIN).stream()
                        .map(roleName -> roleRepository.findByName(roleName).orElseThrow())
                        .collect(java.util.stream.Collectors.toSet());
                admin.getRoles().addAll(adminRoles);
                log.info("Creating default admin user with email admin@ecommerce.local");
                return userRepository.save(admin);
            });
        };
    }
}
