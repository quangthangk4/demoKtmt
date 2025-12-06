package com.ktmt.demoapplication.content.infrastructure.persistence.mapper;

import com.ktmt.demoapplication.content.domain.model.Category;
import com.ktmt.demoapplication.content.infrastructure.persistence.entity.CategoryJpaEntity;

import java.util.UUID;

public final class CategoryMapper {

    private CategoryMapper() {}

    public static Category toDomain(CategoryJpaEntity e) {
        if (e == null) return null;
        return Category.reconstitute(
                e.getId(),
                e.getName(),
                e.getDescription(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    public static CategoryJpaEntity toEntity(Category c) {
        if (c == null) return null;
        UUID id = c.getId(); // Note: Category.getId() returns UUID in your domain
        return new CategoryJpaEntity(
                id,
                c.getName(),
                c.getDescription(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
