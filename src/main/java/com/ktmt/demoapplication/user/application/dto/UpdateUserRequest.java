package com.ktmt.demoapplication.user.application.dto;

/**
 * DTO for updating user information
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for carrying user update data
 */
public record UpdateUserRequest(
    String firstName,
    String lastName,
    String email,
    Integer age
) {
}
