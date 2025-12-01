package com.ktmt.demoapplication.user.application.dto;

import com.ktmt.demoapplication.user.domain.model.User;

import java.time.LocalDateTime;

/**
 * DTO for user response
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for carrying user data for response
 * - Open/Closed Principle (OCP): Can be extended without modifying existing code
 */
public record UserResponse(
    String id,
    String firstName,
    String lastName,
    String fullName,
    String email,
    Integer age,
    boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId().toString(),
            user.getFirstName(),
            user.getLastName(),
            user.getFullName(),
            user.getEmail().getValue(),
            user.getAge(),
            user.isActive(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
