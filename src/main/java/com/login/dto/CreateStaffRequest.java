package com.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Create staff request payload - only accessible by ADMIN users")
public class CreateStaffRequest {

    @Schema(
        description = "Staff member's unique username (3-50 characters)",
        example = "jane.staff",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 3,
        maxLength = 50
    )
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Schema(
        description = "Staff member's email address (must be unique)",
        example = "jane@company.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(
        description = "Staff member's password (minimum 6 characters)",
        example = "staffpass123",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 6
    )
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Schema(
        description = "Must match the password field exactly",
        example = "staffpass123",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @Schema(
        description = "Staff member's full display name (optional, defaults to username if not provided)",
        example = "Jane Smith",
        nullable = true
    )
    private String fullName;

    public CreateStaffRequest() {}

    public CreateStaffRequest(String username, String email, String password, String confirmPassword, String fullName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.fullName = fullName;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}
