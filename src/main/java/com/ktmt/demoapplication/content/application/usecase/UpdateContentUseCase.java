package com.ktmt.demoapplication.content.application.usecase;

import com.ktmt.demoapplication.content.application.dto.UpdateContentRequest;
import com.ktmt.demoapplication.content.domain.model.ContentId;
import com.ktmt.demoapplication.content.domain.repository.IContentRepository;
import com.ktmt.demoapplication.content.domain.service.ContentDomainService;
import com.ktmt.demoapplication.content.domain.model.Content;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UpdateContentUseCase {
    private final IContentRepository iContentRepository;
    private final ContentDomainService contentDomainService;

    public UpdateContentUseCase(IContentRepository iContentRepository, ContentDomainService contentDomainService){
        this.contentDomainService = contentDomainService;
        this.iContentRepository = iContentRepository;
    }

    public void execute(String contentId, UpdateContentRequest req){
        ContentId id = ContentId.from(contentId);
        Content existingContent = iContentRepository.getItem(id)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with id: " + id));

        UUID topic = UUID.fromString(req.topic());
        contentDomainService.ensureCategoryTopicExists(topic);
        contentDomainService.ensureTitleIsUniqueForUpdate(req.title(), existingContent.getId());

        existingContent.updateInformation(
                req.title(),
                req.description(),
                req.topic()
        );

        iContentRepository.updateItem(existingContent);
    }
}
