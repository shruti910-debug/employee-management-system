package com.example.employee.controller;




import com.example.employee.dto.LoginRequest;
import com.example.employee.entity.User;
import com.example.employee.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    // Register User
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return authService.registerUser(user); // Call registerUser(), not authenticate()
    }

    // Login & Generate JWT Token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.authenticate(request.getUsername(), request.getPassword());
    }
}
