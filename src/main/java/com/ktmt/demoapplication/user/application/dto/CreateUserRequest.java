package com.ktmt.demoapplication.user.application.dto;

/**
 * DTO for creating a new user
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for carrying user creation data
 * - Data Transfer Object pattern: Separates domain model from application layer concerns
 */
public record CreateUserRequest(
    String firstName,
    String lastName,
    String email,
    Integer age
) {
}
