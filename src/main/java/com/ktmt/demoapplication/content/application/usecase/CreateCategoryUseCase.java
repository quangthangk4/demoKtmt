package com.ktmt.demoapplication.content.application.usecase;

import com.ktmt.demoapplication.content.application.dto.CategoryResponse;
import com.ktmt.demoapplication.content.application.dto.CreateCategoryRequest;
import com.ktmt.demoapplication.content.domain.model.Category;
import com.ktmt.demoapplication.content.domain.repository.ICategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateCategoryUseCase {
    private final ICategoryRepository iCategoryRepository;

    public CreateCategoryUseCase(ICategoryRepository iCategoryRepository){
        this.iCategoryRepository = iCategoryRepository;
    }

    public CategoryResponse execute(CreateCategoryRequest req){
        Category category = Category.create(
            req.name(),
            req.description()
        );

        Category savedCategory = iCategoryRepository.addCategory(category);

        return CategoryResponse.from(savedCategory);
    }
}
