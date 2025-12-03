package com.ktmt.demoapplication.content.application.dto;

import com.ktmt.demoapplication.content.domain.model.Category;

import java.time.LocalDateTime;

public record CategoryResponse(
        String categoryId,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CategoryResponse from(Category category){
        return new CategoryResponse(
                category.getId().toString(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
