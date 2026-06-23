package com.login.controller;

import com.login.dto.*;
import com.login.model.User;
import com.login.security.JwtUtils;
import com.login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "🔑 PUBLIC - Login and Register endpoints (no token required)")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Operation(
        summary = "🔐 LOGIN - Authenticate and get JWT token",
        description = """
            === HOW TO INTEGRATE THIS IN YOUR FRONTEND ===
            
            **📋 STEP 1: Create a Login Form**
            - Add two input fields: email and password
            - Add a "Sign In" button
            - Add loading state while waiting for response
            
            **📋 STEP 2: Call this endpoint**
            ```
            POST /api/auth/login
            Content-Type: application/json
            
            {
              "email": "admin@system.com",
              "password": "admin123"
            }
            ```
            
            **📋 STEP 3: Handle the Response (200 Success)**
            ```json
            {
              "success": true,
              "message": "Login successful",
              "data": {
                "token": "eyJhbGciOiJIUzI1NiJ9...",
                "type": "Bearer",
                "id": 1,
                "email": "admin@system.com",
                "username": "admin",
                "fullName": "System Administrator",
                "role": "ADMIN"
              }
            }
            ```
            ✅ Save the token: localStorage.setItem('token', data.data.token)
            ✅ Save user info: localStorage.setItem('user', JSON.stringify(data.data))
            ✅ Check role: data.data.role → redirect to correct dashboard
            ✅ Role options: 'ADMIN' → /admin, 'STAFF' → /staff, 'CUSTOMER' → /customer
            
            **📋 STEP 4: Handle the Response (401 Error)**
            ```json
            {
              "success": false,
              "message": "Invalid email or password",
              "data": null
            }
            ```
            ❌ Show error message to user
            ❌ Don't redirect - let user try again
            
            **📋 STEP 5: Test Credentials (after running init.sql)**
            - ADMIN: admin@system.com / admin123
            - Or register a new account via POST /api/auth/register
            """,
        security = {}
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String email = auth.getName();
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());

            JwtResponse jwtResponse = new JwtResponse(
                    token, user.getId(), user.getEmail(),
                    user.getUsername(), user.getFullName(), user.getRole()
            );

            return ResponseEntity.ok(ApiResponse.success("Login successful", jwtResponse));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid email or password"));
        }
    }

    @Operation(
        summary = "📝 REGISTER - Create a new customer account",
        description = """
            === HOW TO INTEGRATE THIS IN YOUR FRONTEND ===
            
            **📋 STEP 1: Create a Registration Form**
            - Add three input fields: email, password, confirm password
            - Add a "Create Account" button
            - Add client-side validation:
              • Email looks valid (contains @)
              • Password is at least 6 characters
              • Password and confirm password match
            
            **📋 STEP 2: Call this endpoint**
            ```
            POST /api/auth/register
            Content-Type: application/json
            
            {
              "email": "john@example.com",
              "password": "mypassword123",
              "confirmPassword": "mypassword123"
            }
            ```
            
            **📋 STEP 3: Handle the Response (201 Created)**
            ```json
            {
              "success": true,
              "message": "Registration successful! You can now login.",
              "data": null
            }
            ```
            ✅ Show success message to user
            ✅ Redirect to login page
            
            **📋 STEP 4: Handle the Response (400 Error)**
            ```json
            {
              "success": false,
              "message": "Email is already registered",
              "data": null
            }
            ```
            ❌ Show error message (could be: "Email is already registered" or "Passwords do not match")
            ❌ Let user fix the issue and try again
            
            **📋 IMPORTANT NOTES:**
            - This endpoint is ONLY for CUSTOMER role registration
            - Username is auto-generated from the email prefix (e.g., john@email.com → "john")
            - Staff accounts are created by ADMIN via POST /api/admin/staff
            - After successful registration, the user must log in via POST /api/auth/login
            """,
        security = {}
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            userService.registerCustomer(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Registration successful! You can now login.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
