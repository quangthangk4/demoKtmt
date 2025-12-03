package com.ktmt.demoapplication.content.domain.repository;

import com.ktmt.demoapplication.content.domain.model.Content;
import com.ktmt.demoapplication.content.domain.model.ContentId;

import java.util.List;
import java.util.Optional;

/**
 * Content Repository Interface (Port in Hexagonal Architecture)
 * Corresponds to IContentRepository in the Class Diagram.
 *
 * SOLID Principles Applied:
 * - Dependency Inversion Principle (DIP): High-level domain doesn't depend on low-level infrastructure
 * - Interface Segregation Principle (ISP): Focused interface with only necessary methods for Content
 * - Single Responsibility Principle (SRP): Responsible only for Content persistence abstraction
 */
public interface IContentRepository {

    /**
     * Retrieve a Content item by its ID (Port method: getItem)
     */
    Optional<Content> getItem(ContentId id);

    /**
     * Retrieve all Content items (Port method: getAllItems)
     */
    List<Content> getAllItems();

    /**
     * Search Content items based on criteria (Port method: searchItems)
     */
    List<Content> searchItems(String criteria);

    /**
     * Add new Content (Port method: addItem)
     * Returns the persisted Content object.
     */
    Content addItem(Content content);

    /**
     * Update existing Content (Port method: updateItem)
     */
    void updateItem(Content content);

    /**
     * Delete Content by ID (Port method: deleteItem)
     */
    void deleteItem(ContentId id);
}