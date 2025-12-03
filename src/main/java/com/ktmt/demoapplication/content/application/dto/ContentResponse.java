package com.ktmt.demoapplication.content.application.dto;

import com.ktmt.demoapplication.content.domain.model.Content;
import com.ktmt.demoapplication.content.domain.model.ContentId;

import java.time.LocalDateTime;
import java.util.Objects;

public record ContentResponse(
        String contentId,
        String title,
        String description,
        String type,
        String topic,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ContentResponse from(Content content){
        return new ContentResponse(
                content.getId().toString(),
                content.getTitle(),
                content.getDescription(),
                content.getType(),
                content.getTopic(),
                content.getCreatedBy(),
                content.getCreatedAt(),
                content.getUpdatedAt()
        );
    }
}
