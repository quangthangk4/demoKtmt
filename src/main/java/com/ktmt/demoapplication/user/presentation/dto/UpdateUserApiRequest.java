package com.ktmt.demoapplication.user.presentation.dto;

import com.ktmt.demoapplication.user.application.dto.UpdateUserRequest;
import jakarta.validation.constraints.*;

/**
 * API Request DTO for updating a user
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for API request validation
 */
public record UpdateUserApiRequest(
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    String firstName,

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    String lastName,

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be at least 0")
    @Max(value = 150, message = "Age must not exceed 150")
    Integer age
) {
    public UpdateUserRequest toApplicationDto() {
        return new UpdateUserRequest(firstName, lastName, email, age);
    }
}
