package com.ktmt.demoapplication.content.domain.repository;

import com.ktmt.demoapplication.content.domain.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Category Repository Interface (Port in Hexagonal Architecture)
 * Corresponds to ICategoryRepository in the Class Diagram.
 *
 * SOLID Principles Applied:
 * - Dependency Inversion Principle (DIP): Domain doesn't depend on low-level storage details
 * - Interface Segregation Principle (ISP): Focused interface with methods strictly for Category
 * - Single Responsibility Principle (SRP): Responsible only for Category persistence abstraction
 */
public interface ICategoryRepository {

    /**
     * Retrieve a Category by its ID (Port method: getCategory)
     */
    Optional<Category> getCategory(UUID id);

    /**
     * Retrieve all Categories (Port method: getCategories)
     */
    List<Category> getCategories();

    /**
     * Add new Category (Port method: addCategory)
     * Returns the persisted Category object.
     */
    Category addCategory(Category category);

    /**
     * Update existing Category (Port method: updateCategory)
     */
    void updateCategory(Category category);

    /**
     * Delete Category by ID (Port method: deleteCategory)
     */
    void deleteCategory(UUID id);
}