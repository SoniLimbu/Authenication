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
@RequestMapping("/api/customer")
@Tag(name = "Customer", description = "👥 CUSTOMER ACCESS - Requires any authenticated role and JWT token")
public class CustomerController {

    private final UserService userService;

    public CustomerController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
        summary = "👥 Get Customer Profile - View your own profile",
        description = """
            === HOW TO INTEGRATE THIS IN YOUR FRONTEND ===
            
            **📋 STEP 1: Customer registers and logs in (gets JWT token)**
            
            **📋 STEP 2: Navigate to customer dashboard/profile page**
            
            **📋 STEP 3: Call this endpoint to get customer profile:**
            ```
            GET /api/customer/profile
            Authorization: Bearer <customer-token>
            ```
            
            **📋 STEP 4: Display profile information**
            - Show username (auto-generated from email), email, member since date
            - Note: fullName may be null for customers who registered without it
            
            **📋 IMPORTANT NOTES:**
            - This endpoint works for ALL authenticated users (CUSTOMER, STAFF, ADMIN)
            - The profile returned is the currently logged-in user's profile
            - Customers can see their auto-generated username here
            """
    )
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> getProfile(Principal principal) {
        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved", UserDto.fromEntity(user)));
    }
}
