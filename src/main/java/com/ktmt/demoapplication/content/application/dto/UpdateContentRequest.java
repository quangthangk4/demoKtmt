package com.ktmt.demoapplication.content.application.dto;

public record UpdateContentRequest(
        String title,
        String description,
        String topic) {
}
