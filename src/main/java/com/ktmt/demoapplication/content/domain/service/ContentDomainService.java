package com.ktmt.demoapplication.content.domain.service;

import com.ktmt.demoapplication.content.domain.model.Content;
import com.ktmt.demoapplication.content.domain.model.ContentId;
import com.ktmt.demoapplication.content.domain.repository.ICategoryRepository;
import com.ktmt.demoapplication.content.domain.repository.IContentRepository;

// Import từ User Module để thực hiện kiểm tra liên Module
import com.ktmt.demoapplication.user.domain.model.User;
import com.ktmt.demoapplication.user.domain.model.UserId;
import com.ktmt.demoapplication.user.domain.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Content Domain Service: Xử lý logic nghiệp vụ phức tạp liên quan đến
 * nhiều Aggregate Roots (Content, Category) hoặc liên Module (Content, User),
 * và các quy tắc cần truy cập Repository.
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Chỉ xử lý các quy tắc điều phối miền.
 * - Dependency Inversion Principle (DIP): Phụ thuộc vào Repository Abstractions (Interfaces) từ các Module khác nhau.
 */
@Service
public class ContentDomainService {

    private final IContentRepository contentRepository;
    private final ICategoryRepository categoryRepository;
    private final UserRepository userRepository; // Dependency liên Module

    // Sử dụng Constructor Injection để tiêm các Repository Interfaces cần thiết
    public ContentDomainService(IContentRepository contentRepository, 
                                ICategoryRepository categoryRepository,
                                UserRepository userRepository) {
        this.contentRepository = contentRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    

    /**
     * Quy tắc Miền: Đảm bảo tiêu đề của một Nội dung mới là duy nhất.
     */
    public void ensureTitleIsUnique(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        
        // Sử dụng phương thức tìm kiếm và kiểm tra trùng khớp tiêu đề
        List<Content> matchingContent = contentRepository.searchItems(title);
        
        boolean exactMatchFound = matchingContent.stream()
            // So sánh không phân biệt chữ hoa, chữ thường
            .anyMatch(content -> content.getTitle().equalsIgnoreCase(title));

        if (exactMatchFound) {
            throw new IllegalStateException("Content with title '" + title + "' already exists.");
        }
    }

    /**
     * Quy tắc Miền: Đảm bảo tiêu đề là duy nhất khi cập nhật, loại trừ chính Content đang được cập nhật.
     */
    public void ensureTitleIsUniqueForUpdate(String newTitle, ContentId currentContentId) {
        if (newTitle == null || newTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("New title cannot be empty.");
        }

        List<Content> matchingContent = contentRepository.searchItems(newTitle);
        
        boolean conflictFound = matchingContent.stream()
            .anyMatch(content -> content.getTitle().equalsIgnoreCase(newTitle) && 
                                 // Đảm bảo Content tìm thấy không phải là Content hiện tại
                                 !content.getId().equals(currentContentId));

        if (conflictFound) {
            throw new IllegalStateException("Cannot update: Content with title '" + newTitle + "' already exists for another item.");
        }
    }

    /**
     * Quy tắc Miền: Đảm bảo Category (Topic) được tham chiếu bởi Content tồn tại.
     * Đây là logic liên Aggregate (Content phụ thuộc vào Category).
     */
    public void ensureCategoryTopicExists(UUID categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null when ensuring existence.");
        }
        
        if (categoryRepository.getCategory(categoryId).isEmpty()) {
            throw new IllegalStateException("The referenced Topic/Category with ID " + categoryId + " does not exist.");
        }
    }

    /**
     * Quy tắc Miền: Đảm bảo người tạo (Instructor) tồn tại và đang hoạt động (active).
     * Đây là logic liên Module (Content phụ thuộc vào User), cần UserRepository.
     */
    public void ensureCreatorExistsAndIsActive(String createdByIdString) {
        if (createdByIdString == null || createdByIdString.trim().isEmpty()) {
             throw new IllegalArgumentException("Creator ID cannot be null or empty.");
        }
        
        UserId userId;
        try {
            // Chuyển đổi String ID sang Value Object UserId
            userId = UserId.from(createdByIdString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid format for Creator ID.", e);
        }
        
        // Dùng UserRepository để kiểm tra User
        Optional<User> userOptional = userRepository.findById(userId);

        userOptional.ifPresentOrElse(
            user -> {
                // Kiểm tra trạng thái hoạt động (quy tắc nghiệp vụ)
                if (!user.isActive()) { 
                    throw new IllegalStateException("The creator specified is inactive.");
                }
            },
            () -> {
                // User không tồn tại
                throw new IllegalStateException("The creator specified does not exist.");
            }
        );
    }
}