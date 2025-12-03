package com.ktmt.demoapplication.content.application.dto;

import com.ktmt.demoapplication.content.domain.model.ContentId;

import java.time.LocalDateTime;

public record CreateContentRequest(
        String title,
        String description,
        String type,
        String topic,
        String createdBy) {
}
