package com.example.smart_expense_manager.controller;

import com.example.smart_expense_manager.entity.User;
import com.example.smart_expense_manager.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // BCrypt password encoder
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();


    // =========================
    // REGISTER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        try {

            if (user.getName() == null || user.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Name is required");
            }

            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Email is required");
            }

            if (user.getPassword() == null || user.getPassword().length() < 6) {
                return ResponseEntity.badRequest()
                        .body("Password must contain at least 6 characters");
            }

            User existingUser =
                    userRepository.findByEmail(user.getEmail());

            if (existingUser != null) {
                return ResponseEntity.badRequest()
                        .body("Email already registered");
            }

            // Encrypt password before saving
            user.setPassword(
                    passwordEncoder.encode(user.getPassword())
            );

            User savedUser = userRepository.save(user);

            return ResponseEntity.ok(savedUser);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(500)
                    .body("Registration database error: " + e.getMessage());
        }
    }


    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {

        try {

            User existingUser =
                    userRepository.findByEmail(user.getEmail());

            if (existingUser != null &&
                    passwordEncoder.matches(
                            user.getPassword(),
                            existingUser.getPassword())) {

                return ResponseEntity.ok(existingUser);
            }

            return ResponseEntity
                    .status(401)
                    .body("Invalid email or password");

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(500)
                    .body("Login error: " + e.getMessage());
        }
    }


    // =========================
    // FORGOT PASSWORD - VERIFY EMAIL
    // =========================

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody User user) {

        try {

            if (user.getEmail() == null ||
                    user.getEmail().trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Email is required");
            }

            User existingUser =
                    userRepository.findByEmail(user.getEmail());

            if (existingUser == null) {

                return ResponseEntity
                        .status(404)
                        .body("Email not registered");
            }

            return ResponseEntity.ok(
                    "Email verified successfully"
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(500)
                    .body("Forgot password error: " + e.getMessage());
        }
    }


    // =========================
    // RESET PASSWORD
    // =========================

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody User user) {

        try {

            if (user.getEmail() == null ||
                    user.getEmail().trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Email is required");
            }

            if (user.getPassword() == null ||
                    user.getPassword().length() < 6) {

                return ResponseEntity
                        .badRequest()
                        .body("Password must contain at least 6 characters");
            }

            User existingUser =
                    userRepository.findByEmail(user.getEmail());

            if (existingUser == null) {

                return ResponseEntity
                        .status(404)
                        .body("Email not registered");
            }

            // Encrypt new password
            existingUser.setPassword(
                    passwordEncoder.encode(user.getPassword())
            );

            userRepository.save(existingUser);

            return ResponseEntity.ok(
                    "Password reset successfully"
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(500)
                    .body("Password reset error: " + e.getMessage());
        }
    }
}