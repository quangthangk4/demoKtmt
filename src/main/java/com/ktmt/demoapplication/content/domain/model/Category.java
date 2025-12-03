package com.ktmt.demoapplication.content.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Category Entity (potentially an Aggregate Root)
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Manages category business logic only.
 * - Encapsulation: Business rules are encapsulated within the domain model.
 */
public class Category {
    private final UUID id; // Dòng 22 (Lỗi 1)
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Private constructor: Phải nhận ID để gán giá trị cho final field
    private Category(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        this.id = id;
    }

    /**
     * Factory method to create new Category
     */
    public static Category create(String name, String description) {
        // Khởi tạo đối tượng bằng cách gọi constructor và truyền ID
        Category category = new Category(UUID.randomUUID()); 
        category.setName(name);
        category.setDescription(description);
        category.createdAt = LocalDateTime.now();
        category.updatedAt = LocalDateTime.now();
        return category;
    }

    /**
     * Factory method to reconstitute Category from persistence
     */
    public static Category reconstitute(UUID id, String name, String description,
                                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        // Khởi tạo đối tượng bằng cách gọi constructor và truyền ID
        Category category = new Category(id); 
        category.name = name;
        category.description = description;
        category.createdAt = createdAt;
        category.updatedAt = updatedAt;
        return category;
    }

    /**
     * Business method to update category information
     */
    public void updateInformation(String name, String description) {
        setName(name);
        setDescription(description);
        this.updatedAt = LocalDateTime.now();
    }

    // Setters with business validation
    private void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Category name cannot exceed 100 characters");
        }
        this.name = name.trim();
    }

    private void setDescription(String description) {
        this.description = description;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}