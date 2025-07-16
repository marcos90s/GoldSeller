package com.marcos90s.goldSellerAPI.dto;

import com.marcos90s.goldSellerAPI.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsersRequestDTO {
    @NotBlank(message = "Field Name is required")
    private String name;
    @NotBlank(message = "Field Email is required")
    @Email(message = "Invalid email format")

    private String email;
    @NotBlank(message = "Field Password is required")
    private String password;
    private UserRole role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
