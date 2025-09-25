package com.example.userservice.controller;

import com.example.userservice.dto.AddressRequest;
import com.example.userservice.dto.AddressResponse;
import com.example.userservice.dto.ChangePasswordRequest;
import com.example.userservice.dto.UpdateProfileRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        User user = userService.getByEmail(authentication.getName());
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }

    @PutMapping("/me/profile")
    public ResponseEntity<UserResponse> updateProfile(Authentication authentication,
                                                      @Valid @RequestBody UpdateProfileRequest request) {
        User user = userService.getByEmail(authentication.getName());
        User updated = userService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(UserResponse.fromEntity(updated));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(Authentication authentication,
                                               @Valid @RequestBody ChangePasswordRequest request) {
        User user = userService.getByEmail(authentication.getName());
        userService.changePassword(user.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/addresses")
    public ResponseEntity<AddressResponse> addAddress(Authentication authentication,
                                                      @Valid @RequestBody AddressRequest request) {
        User user = userService.getByEmail(authentication.getName());
        AddressResponse address = AddressResponse.fromEntity(userService.addAddress(user.getId(), request));
        return ResponseEntity.ok(address);
    }

    @DeleteMapping("/me/addresses/{addressId}")
    public ResponseEntity<Void> removeAddress(Authentication authentication, @PathVariable UUID addressId) {
        User user = userService.getByEmail(authentication.getName());
        userService.removeAddress(user.getId(), addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(UserResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(users);
    }
}
