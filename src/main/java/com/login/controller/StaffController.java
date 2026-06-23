package com.login.controller;

import com.login.dto.ApiResponse;
import com.login.dto.UserDto;
import com.login.model.User;
import com.login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/staff")
@Tag(name = "Staff", description = "👤 STAFF ACCESS - Requires STAFF or ADMIN role and JWT token")
public class StaffController {

    private final UserService userService;

    public StaffController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
        summary = "👤 Get Staff Profile - View your own profile",
        description = """
            === HOW TO INTEGRATE THIS IN YOUR FRONTEND ===
            
            **📋 STEP 1: Staff member logs in (gets JWT token)**
            
            **📋 STEP 2: Navigate to staff dashboard/profile page**
            
            **📋 STEP 3: Call this endpoint to get staff profile:**
            ```
            GET /api/staff/profile
            Authorization: Bearer <staff-token>
            ```
            
            **📋 STEP 4: Display profile information**
            - Show username, email, full name, role, member since date
            
            **📋 IMPORTANT NOTES:**
            - This endpoint only works for STAFF and ADMIN roles
            - A CUSTOMER calling this will get a 403 Forbidden
            - The profile returned is the currently logged-in user's profile
            """
    )
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> getProfile(Principal principal) {
        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved", UserDto.fromEntity(user)));
    }
}
