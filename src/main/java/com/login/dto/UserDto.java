package com.login.dto;

import com.login.model.Role;
import com.login.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "User data object returned when listing or viewing users")
public class UserDto {

    @Schema(description = "Unique user ID", example = "1")
    private Long id;

    @Schema(
        description = "Username (auto-generated for customers from email prefix)",
        example = "admin",
        nullable = true
    )
    private String username;

    @Schema(description = "Email address (used for login)", example = "admin@system.com")
    private String email;

    @Schema(description = "Full display name", example = "System Administrator", nullable = true)
    private String fullName;

    @Schema(
        description = "User role - determines permissions",
        example = "ADMIN",
        allowableValues = {"ADMIN", "STAFF", "CUSTOMER"}
    )
    private Role role;

    @Schema(description = "Whether the user account is active. Disabled users cannot log in.", example = "true")
    private boolean enabled;

    @Schema(description = "When the account was created", example = "2026-06-23T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "When the account was last updated", example = "2026-06-23T12:00:00")
    private LocalDateTime updatedAt;

    public UserDto() {}

    public static UserDto fromEntity(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
