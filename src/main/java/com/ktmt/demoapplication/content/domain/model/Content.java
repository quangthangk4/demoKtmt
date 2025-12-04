package com.ktmt.demoapplication.content.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Content Aggregate Root (DDD Pattern)
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Manages content business logic only (creation, update, type validation).
 * - Open/Closed Principle (OCP): Open for extension (e.g., adding more content types), closed for modification of core logic.
 * - Encapsulation: Business rules are encapsulated within the domain model.
 */
public class Content {
    private ContentId id;
    private String title;
    private String description;
    private String type; // text, video, quiz
    private String topic; // Relates to Category
    private String createdBy; // ID of the instructor/creator
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Danh sách các loại nội dung hợp lệ
    private static final String[] VALID_TYPES = {"text", "video", "quiz", "interactive_lab"};

    // Private constructor to enforce creation through factory methods
    private Content() {
    }

    /**
     * Factory method to create new Content
     */
    public static Content create(String title, String description, String type, String topic, String createdBy) {
        Content content = new Content();
        content.id = ContentId.create();
        content.setTitle(title);
        content.setDescription(description);
        content.setType(type);
        content.setTopic(topic);
        content.setCreatedBy(createdBy);
        content.createdAt = LocalDateTime.now();
        content.updatedAt = LocalDateTime.now();
        return content;
    }

    /**
     * Factory method to reconstitute Content from persistence
     */
    public static Content reconstitute(ContentId id, String title, String description, String type,
                                       String topic, String createdBy, LocalDateTime createdAt,
                                       LocalDateTime updatedAt) {
        Content content = new Content();
        content.id = id;
        content.title = title;
        content.description = description;
        content.type = type;
        content.topic = topic;
        content.createdBy = createdBy;
        content.createdAt = createdAt;
        content.updatedAt = updatedAt;
        return content;
    }

    /**
     * Business method to update core content information
     */
    public void updateInformation(String title, String description, String topic) {
        setTitle(title);
        setDescription(description);
        setTopic(topic);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Business method to change content type (if allowed)
     */
    public void changeType(String newType) {
        setType(newType);
        this.updatedAt = LocalDateTime.now();
    }

    // Setters with business validation
    private void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (title.length() > 255) {
            throw new IllegalArgumentException("Title cannot exceed 255 characters");
        }
        this.title = title.trim();
    }

    private void setDescription(String description) {
        // Description can be null/empty, but logic ensures it's clean if provided
        this.description = (description != null) ? description.trim() : null;
    }

    private void setType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        String normalizedType = type.toLowerCase().trim();
        boolean isValid = false;
        for (String validType : VALID_TYPES) {
            if (validType.equals(normalizedType)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new IllegalArgumentException("Invalid content type: " + type);
        }
        this.type = normalizedType;
    }
    
    private void setTopic(String topic) {
        if (topic == null || topic.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic cannot be null or empty");
        }
        // Lưu ý: Việc kiểm tra Topic (Category) có tồn tại trong hệ thống hay không
        // thường được thực hiện ở tầng Application Service/Domain Service,
        // sử dụng CategoryRepository.
        this.topic = topic.trim();
    }

    private void setCreatedBy(String createdBy) {
        if (createdBy == null || createdBy.trim().isEmpty()) {
            throw new IllegalArgumentException("CreatedBy ID cannot be null or empty");
        }
        this.createdBy = createdBy.trim();
    }
    
    // Getters
    public ContentId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getTopic() {
        return topic;
    }

    public String getCreatedBy() {
        return createdBy;
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
        Content content = (Content) o;
        return Objects.equals(id, content.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}