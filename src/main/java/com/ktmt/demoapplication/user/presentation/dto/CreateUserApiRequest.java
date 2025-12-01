package com.ktmt.demoapplication.user.presentation.dto;

import com.ktmt.demoapplication.user.application.dto.CreateUserRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * API Request DTO for creating a user
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for API request validation
 * - Separation of Concerns: API layer DTOs separated from application layer DTOs
 */
@Schema(description = "Request body for creating a new user")
public record CreateUserApiRequest(
    @Schema(description = "User's first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    String firstName,

    @Schema(description = "User's last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    String lastName,

    @Schema(description = "User's email address", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,

    @Schema(description = "User's age", example = "30", minimum = "0", maximum = "150", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be at least 0")
    @Max(value = 150, message = "Age must not exceed 150")
    Integer age
) {
    public CreateUserRequest toApplicationDto() {
        return new CreateUserRequest(firstName, lastName, email, age);
    }
}
