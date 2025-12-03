package com.ktmt.demoapplication.content.application.usecase;

import com.ktmt.demoapplication.content.application.dto.ContentResponse;
import com.ktmt.demoapplication.content.domain.model.Content;
import com.ktmt.demoapplication.content.domain.model.ContentId;
import com.ktmt.demoapplication.content.domain.repository.IContentRepository;
import com.ktmt.demoapplication.content.domain.service.ContentDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetContentUseCase {
    private final IContentRepository iContentRepository;

    public GetContentUseCase(IContentRepository iContentRepository){
        this.iContentRepository = iContentRepository;
    }

    public ContentResponse getContentById(String contentId){
        ContentId id = ContentId.from(contentId);
        Content existingContent = iContentRepository.getItem(id)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with id: " + id));
        return ContentResponse.from(existingContent);
    }

    public List<ContentResponse> getAllContent(){
        List<Content> allContents = iContentRepository.getAllItems();

        return allContents.stream()
                .map(ContentResponse::from)
                .toList();
    }

    public List<ContentResponse> searchContent(String cond){
        List<Content> searchedContent = iContentRepository.searchItems(cond);

        return searchedContent.stream()
                .map(ContentResponse::from)
                .toList();
    }
}
