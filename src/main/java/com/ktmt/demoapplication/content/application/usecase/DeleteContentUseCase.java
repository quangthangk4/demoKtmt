package com.ktmt.demoapplication.content.application.usecase;

import com.ktmt.demoapplication.content.application.dto.ContentResponse;
import com.ktmt.demoapplication.content.domain.model.Content;
import com.ktmt.demoapplication.content.domain.model.ContentId;
import com.ktmt.demoapplication.content.domain.repository.IContentRepository;
import com.ktmt.demoapplication.content.domain.service.ContentDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteContentUseCase {
    private final IContentRepository iContentRepository;

    public DeleteContentUseCase(IContentRepository iContentRepository){
        this.iContentRepository = iContentRepository;
    }

    public void execute(String contentId){
        ContentId id = ContentId.from(contentId);
        Content existingContent = iContentRepository.getItem(id)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with id: " + id));

        iContentRepository.deleteItem(existingContent.getId());
    }
}
