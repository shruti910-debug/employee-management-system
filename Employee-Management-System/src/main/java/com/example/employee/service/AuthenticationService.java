package com.example.employee.service;



import com.example.employee.entity.User;
import com.example.employee.repository.UserRepository;
import com.example.employee.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    // User Registration
    @Transactional
    public ResponseEntity<?> registerUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required!");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required!");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists!");
        }

        // ✅ Ensure password is encoded
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // ✅ Debugging: Log password before saving
        System.out.println("DEBUG: Saving user -> " + user.getUsername() + " | " + user.getPassword());

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }


    // User Login & JWT Token Generation
//    public ResponseEntity<?> authenticate(String username, String password) {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//            String token = jwtUtil.generateToken(userDetails.getUsername());
//            return ResponseEntity.ok(token);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Invalid credentials!");
//        }
//    }
//}
    public ResponseEntity<?> authenticate(String username, String password) {
        try {
            Optional<User> userOpt = userRepository.findByUsername(username);

            // ✅ Debugging: Log user details
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found!");
            }

            User user = userOpt.get();
            System.out.println("DEBUG: Retrieved user -> " + user.getUsername() + " | " + user.getPassword());

            // ✅ Check if the password matches
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid credentials! Password does not match.");
            }

            // ✅ Generate JWT Token
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Authentication error: " + e.getMessage());
        }
    }
}
