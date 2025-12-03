package com.ktmt.demoapplication.content.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing Content ID
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Class has single responsibility - represent content identifier.
 * - Immutability: Value object is immutable, preventing unexpected state changes.
 */
public class ContentId {
    private final UUID value;

    private ContentId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Content ID cannot be null");
        }
        this.value = value;
    }

    public static ContentId create() {
        return new ContentId(UUID.randomUUID());
    }

    public static ContentId from(UUID value) {
        return new ContentId(value);
    }

    public static ContentId from(String value) {
        try {
            return new ContentId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Content ID format: " + value);
        }
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentId contentId = (ContentId) o;
        return Objects.equals(value, contentId.value);
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