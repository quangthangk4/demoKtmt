package com.ktmt.demoapplication.content.application.usecase;

import com.ktmt.demoapplication.content.application.dto.ContentResponse;
import com.ktmt.demoapplication.content.application.dto.CreateContentRequest;
import com.ktmt.demoapplication.content.domain.model.Category;
import com.ktmt.demoapplication.content.domain.model.Content;
import com.ktmt.demoapplication.content.domain.service.ContentDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ktmt.demoapplication.content.domain.repository.IContentRepository;

import java.util.UUID;

@Service
@Transactional
public class CreateContentUseCase {
    private final IContentRepository  iContentRepository;
    private final ContentDomainService contentDomainService;

    public CreateContentUseCase(IContentRepository iContentRepository, ContentDomainService contentDomainService){
        this.iContentRepository = iContentRepository;
        this.contentDomainService = contentDomainService;
    }

    public ContentResponse execute(CreateContentRequest req){
        UUID topic = UUID.fromString(req.topic());
        contentDomainService.ensureCategoryTopicExists(topic);
        contentDomainService.ensureTitleIsUnique(req.title());
        contentDomainService.ensureCreatorExistsAndIsActive(req.createdBy());

        Content content = Content.create(
                req.title(),
                req.description(),
                req.type(),
                req.topic(),
                req.createdBy()
        );

        Content savedContent = iContentRepository.addItem(content);

        return ContentResponse.from(savedContent);
    }
}
