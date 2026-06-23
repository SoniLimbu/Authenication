package com.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Customer registration request payload - create a new customer account")
public class RegisterRequest {

    @Schema(
        description = "New user's email address (must be unique)",
        example = "john@example.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(
        description = "Password (minimum 6 characters)",
        example = "mypassword123",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 6
    )
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Schema(
        description = "Must match the password field exactly",
        example = "mypassword123",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    public RegisterRequest() {}

    public RegisterRequest(String email, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
