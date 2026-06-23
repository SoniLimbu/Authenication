package com.login.dto;

import com.login.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT authentication response returned after successful login")
public class JwtResponse {

    @Schema(
        description = "JWT Bearer token. Save this and include it in the Authorization header for all subsequent requests",
        example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBzeXN0ZW0uY29tIiwicm9sZSI6IkFETUlOIn0..."
    )
    private String token;

    @Schema(description = "Token type (always 'Bearer')", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "User's unique ID in the database", example = "1")
    private Long id;

    @Schema(description = "User's email address", example = "admin@system.com")
    private String email;

    @Schema(description = "User's username (auto-generated for customers)", example = "admin")
    private String username;

    @Schema(
        description = "User's full display name",
        example = "System Administrator",
        nullable = true
    )
    private String fullName;

    @Schema(
        description = "User's role - determines what endpoints they can access",
        example = "ADMIN",
        allowableValues = {"ADMIN", "STAFF", "CUSTOMER"}
    )
    private Role role;

    public JwtResponse() {}

    public JwtResponse(String token, Long id, String email, String username, String fullName, Role role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
