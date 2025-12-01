package com.ktmt.demoapplication.user.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing User ID
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Class has single responsibility - represent user identifier
 * - Immutability: Value object is immutable, preventing unexpected state changes
 */
public class UserId {
    private final UUID value;

    private UserId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        this.value = value;
    }

    public static UserId create() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId from(UUID value) {
        return new UserId(value);
    }

    public static UserId from(String value) {
        try {
            return new UserId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid User ID format: " + value);
        }
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
