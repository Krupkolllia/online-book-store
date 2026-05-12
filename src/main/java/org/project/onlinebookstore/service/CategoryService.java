package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.category.CategoryDto;
import org.project.onlinebookstore.dto.category.CategoryRequestDto;
import org.project.onlinebookstore.dto.category.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto findById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryDto);

    CategoryResponseDto update(Long id, CategoryRequestDto categoryDto);

    void deleteById(Long id);
}

