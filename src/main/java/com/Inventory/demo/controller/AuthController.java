package com.Inventory.demo.controller;

import com.Inventory.demo.dto.LoginDto;
import com.Inventory.demo.model.Erole;
import com.Inventory.demo.model.User;
import com.Inventory.demo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.Inventory.demo.dto.ApiResponse;
import java.util.Optional;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody LoginDto user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setRoles(Erole.SELLER);
        userService.saveUser(newUser);
        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDto user) {
        return userService.findByUsername(user.getUsername())
                .map(existingUser -> {
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    if (encoder.matches(user.getPassword(), existingUser.getPassword())) {
                        return ResponseEntity.ok("Login successful");
                    }
                    else {
                        return ResponseEntity.status(401).body("Invalid credentials");
                    }
                })
                .orElseGet(() -> ResponseEntity.status(404).body("User not found"));
    }

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createAdmin(@RequestBody LoginDto user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setRoles(Erole.ADMIN); // Only an admin can do this

        userService.saveUser(newUser);
        return ResponseEntity.ok("Admin registered successfully");
    }

    // 🗑️ Delete user by ID (admin only)
    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        if (userService.deleteById(id)) {
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
        } else {
            return ResponseEntity.status(404).body(ApiResponse.error("User not found"));
        }
    }

    // 📋 Get all users (admin only)
    @GetMapping("/admin/users")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LoginDto>>> getAllUsers() {
        List<LoginDto> users = userService.getAllUsers()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(users));
    }

    // 🔍 Get user by ID (admin only)
    @GetMapping("/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LoginDto>> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        return user.map(value -> ResponseEntity.ok(ApiResponse.success(convertToDTO(value))))
                .orElseGet(() -> ResponseEntity.status(404).body(ApiResponse.error("User not found")));
    }

    // Helper to convert User -> LoginDto
    private LoginDto convertToDTO(User user) {
        LoginDto dto = new LoginDto();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        return dto;
    }

}
