package com.ktmt.demoapplication.content.infrastructure.persistence.mapper;

import com.ktmt.demoapplication.content.domain.model.Content;
import com.ktmt.demoapplication.content.domain.model.ContentId;
import com.ktmt.demoapplication.content.infrastructure.persistence.entity.ContentJpaEntity;

public final class ContentMapper {

    private ContentMapper() {}

    public static Content toDomain(ContentJpaEntity e) {
        if (e == null) return null;
        ContentId id = ContentId.from(e.getId());
        return Content.reconstitute(
                id,
                e.getTitle(),
                e.getDescription(),
                e.getType(),
                e.getTopic(),
                e.getCreatedBy(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    public static ContentJpaEntity toEntity(Content c) {
        if (c == null) return null;
        return new ContentJpaEntity(
                c.getId().getValue(), // ContentId -> UUID
                c.getTitle(),
                c.getDescription(),
                c.getType(),
                c.getTopic(),
                c.getCreatedBy(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
