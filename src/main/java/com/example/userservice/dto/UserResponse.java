package com.example.userservice.dto;

import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserRole;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserResponse {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Set<UserRole> roles;
    private Set<AddressResponse> addresses;
    private Instant createdAt;
    private Instant updatedAt;

    public static UserResponse fromEntity(User user) {
        UserResponse response = new UserResponse();
        response.id = user.getId();
        response.email = user.getEmail();
        response.firstName = user.getFirstName();
        response.lastName = user.getLastName();
        response.phoneNumber = user.getPhoneNumber();
        response.roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        response.addresses = user.getAddresses().stream()
                .map(AddressResponse::fromEntity)
                .collect(Collectors.toSet());
        response.createdAt = user.getCreatedAt();
        response.updatedAt = user.getUpdatedAt();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public Set<AddressResponse> getAddresses() {
        return addresses;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
