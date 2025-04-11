package com.Inventory.demo.controller;

import com.Inventory.demo.dto.LoginDto;
import com.Inventory.demo.model.Erole;
import com.Inventory.demo.model.User;
import com.Inventory.demo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

}
