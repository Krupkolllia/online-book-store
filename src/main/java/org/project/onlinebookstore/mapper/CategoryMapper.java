package org.project.onlinebookstore.mapper;

import org.mapstruct.Mapper;
import org.project.onlinebookstore.config.MapStructConfig;
import org.project.onlinebookstore.dto.category.CategoryDto;
import org.project.onlinebookstore.dto.category.CategoryRequestDto;
import org.project.onlinebookstore.dto.category.CategoryResponseDto;
import org.project.onlinebookstore.model.Category;

@Mapper(config = MapStructConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toModel(CategoryRequestDto dto);
}
