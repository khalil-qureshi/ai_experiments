package com.example.userservice.service;

import com.example.userservice.dto.AddressRequest;
import com.example.userservice.dto.ChangePasswordRequest;
import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.dto.UpdateProfileRequest;
import com.example.userservice.entity.Address;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserRole;
import com.example.userservice.exception.BadRequestException;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        Role role = roleRepository.findByName(UserRole.CUSTOMER)
                .orElseGet(() -> roleRepository.save(createRole(UserRole.CUSTOMER)));
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }
        return user;
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = getById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user);
    }

    public void changePassword(UUID userId, ChangePasswordRequest request) {
        User user = getById(userId);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password does not match");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
    }

    public Address addAddress(UUID userId, AddressRequest request) {
        User user = getById(userId);
        Address address = new Address();
        address.setLine1(request.getLine1());
        address.setLine2(request.getLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setUser(user);
        user.getAddresses().add(address);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        return address;
    }

    public void removeAddress(UUID userId, UUID addressId) {
        User user = getById(userId);
        Address address = user.getAddresses().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        user.getAddresses().remove(address);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private Role createRole(UserRole roleName) {
        Role role = new Role();
        role.setName(roleName);
        return role;
    }
}
