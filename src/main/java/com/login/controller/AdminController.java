package com.login.controller;

import com.login.dto.*;
import com.login.model.Role;
import com.login.model.User;
import com.login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "🛡️ ADMIN ONLY - Requires ADMIN role and JWT token in Authorization header")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
        summary = "👤 Create Staff - Admin creates a new staff member",
        description = """
            === HOW TO INTEGRATE THIS IN YOUR FRONTEND ===
            
            **📋 STEP 1: Show "Create Staff" form (Admin dashboard only)**
            - Add input fields: username, email, password, confirm password, full name
            - Add a "Create Staff" button
            - Only show this to ADMIN users
            
            **📋 STEP 2: Call this endpoint (with Admin's token)**
            ```
            POST /api/admin/staff
            Authorization: Bearer <admin-token>
            Content-Type: application/json
            
            {
              "username": "jane.staff",
              "email": "jane@company.com",
              "password": "staffpass123",
              "confirmPassword": "staffpass123",
              "fullName": "Jane Smith"
            }
            ```
            
            **📋 STEP 3: Handle Success (201)**
            - Show "Staff user created successfully"
            - The new staff can log in immediately with their credentials
            
            **📋 STEP 4: Handle Error (400)**
            - Show error (e.g., "Username is already taken")
            
            **📋 VALIDATION:**
            - Username: 3-50 characters, must be unique
            - Email: must be unique in the system
            - Password: minimum 6 characters
            - confirmPassword: must match password
            """
    )
    @PostMapping("/staff")
    public ResponseEntity<ApiResponse<UserDto>> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        try {
            User staff = userService.createStaff(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Staff user created successfully", UserDto.fromEntity(staff)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
        summary = "📋 List Users - Get all registered users",
        description = """
            === HOW TO INTEGRATE THIS IN YOUR FRONTEND ===
            
            **📋 STEP 1: Admin navigates to "User Management" page**
            **📋 STEP 2: Call this endpoint (with Admin's token)**
            ```
            GET /api/admin/users
            Authorization: Bearer <admin-token>
            ```
            
            **📋 STEP 3: Display users in a table**
            - Show columns: ID, Username, Email, Full Name, Role, Status, Created Date
            - Add action buttons per row: Change Role, Toggle Status, Delete
            
            **📋 RESPONSE DATA:**
            ```json
            {
              "success": true,
              "data": [
                {
                  "id": 1,
                  "username": "admin",
                  "email": "admin@system.com",
                  "fullName": "System Administrator",
                  "role": "ADMIN",
                  "enabled": true,
                  "createdAt": "2026-06-23T12:00:00"
                }
              ]
            }
            ```
            
            **📋 FRONTEND TIP:**
            - Show role as colored badges (ADMIN=red, STAFF=yellow, CUSTOMER=blue)
            - Show status as Active/Disabled toggle
            """
    )
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDto>>> listUsers() {
        List<UserDto> users = userService.getAllUsers().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @Operation(
        summary = "🔍 Get User By ID - Get a single user's details",
        description = """
            Call this to get details of a specific user.
            
            **Frontend use case:** Clicking on a user row to see their full details.
            
            ```
            GET /api/admin/users/1
            Authorization: Bearer <admin-token>
            ```
            
            **Response (200):** Returns the user object
            **Response (404):** "User not found"
            """
    )
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(ApiResponse.success("User found", UserDto.fromEntity(user))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found")));
    }

    @Operation(
        summary = "🔄 Change Role - Change a user's role",
        description = """
            === HOW TO INTEGRATE THIS IN YOUR FRONTEND ===
            
            **📋 STEP 1: Add role dropdown to each user row in the table**
            - Options: ADMIN, STAFF, CUSTOMER
            - Pre-select the user's current role
            
            **📋 STEP 2: When admin selects a new role from dropdown:**
            ```
            PUT /api/admin/users/1/role?role=STAFF
            Authorization: Bearer <admin-token>
            ```
            
            **📋 STEP 3: Handle success**
            - The user's role updates immediately
            - They will have new permissions on next action
            
            **📋 EXAMPLE:**
            - Change a CUSTOMER to STAFF → they can access staff endpoints
            - Change a STAFF to CUSTOMER → they lose staff access
            
            **📋 ROLE HIERARCHY:**
            - ADMIN: can access everything
            - STAFF: can access staff and customer endpoints
            - CUSTOMER: can only access customer endpoints
            """
    )
    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<UserDto>> updateUserRole(@PathVariable Long id,
                                                                @RequestParam Role role) {
        try {
            User user = userService.updateUserRole(id, role);
            return ResponseEntity.ok(ApiResponse.success("User role updated successfully", UserDto.fromEntity(user)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
        summary = "⏸️ Toggle Status - Enable or disable a user account",
        description = """
            === HOW TO INTEGRATE THIS IN YOUR FRONTEND ===
            
            **📋 STEP 1: Add Enable/Disable toggle button to each user row**
            
            **📋 STEP 2: When admin clicks the toggle:**
            ```
            PUT /api/admin/users/1/status
            Authorization: Bearer <admin-token>
            ```
            
            **📋 STEP 3: Handle the toggle**
            - If user was enabled → they become disabled (cannot log in)
            - If user was disabled → they become enabled (can log in again)
            
            **📋 FRONTEND TIP:**
            - Show a switch/toggle UI component
            - Show green/Active when enabled, gray/Disabled when disabled
            - Consider adding a confirmation dialog before toggling
            """
    )
    @PutMapping("/users/{id}/status")
    public ResponseEntity<ApiResponse<UserDto>> toggleUserStatus(@PathVariable Long id) {
        try {
            User user = userService.toggleUserEnabled(id);
            String status = user.isEnabled() ? "enabled" : "disabled";
            return ResponseEntity.ok(ApiResponse.success("User has been " + status, UserDto.fromEntity(user)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
        summary = "🗑️ Delete User - Permanently remove a user",
        description = """
            === HOW TO INTEGRATE THIS IN YOUR FRONTEND ===
            
            **📋 STEP 1: Add Delete button to each user row (only for admin)**
            
            **📋 STEP 2: Show confirmation dialog**
            - "Are you sure you want to delete user: {username}?"
            - This is permanent and cannot be undone!
            
            **📋 STEP 3: If admin confirms:**
            ```
            DELETE /api/admin/users/1
            Authorization: Bearer <admin-token>
            ```
            
            **📋 STEP 4: Handle result**
            - On success (200): Remove user from the table, show success message
            - On error (400): Show error message
            
            **⚠️ WARNING:**
            - Deleting a user is PERMANENT
            - Consider disabling the user instead (PUT /api/admin/users/{id}/status)
            - Disabled users cannot log in but their data remains
            """
    )
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
        summary = "📊 Get Dashboard Stats - System statistics",
        description = """
            Get overview statistics for the admin dashboard.
            
            **📋 Frontend use:** Display cards showing:
            - Total Users
            - Admin count
            - Staff count
            - Customer count
            
            ```
            GET /api/admin/stats
            Authorization: Bearer <admin-token>
            ```
            
            **Response:**
            ```json
            {
              "success": true,
              "data": {
                "totalUsers": 6,
                "adminCount": 1,
                "staffCount": 2,
                "customerCount": 3
              }
            }
            ```
            """
    )
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStats>> getStats() {
        DashboardStats stats = new DashboardStats(
                userService.getUserCount(),
                userService.getUserCountByRole(Role.ADMIN),
                userService.getUserCountByRole(Role.STAFF),
                userService.getUserCountByRole(Role.CUSTOMER)
        );
        return ResponseEntity.ok(ApiResponse.success("Stats retrieved", stats));
    }

    public record DashboardStats(long totalUsers, long adminCount, long staffCount, long customerCount) {}
}
