package org.project.onlinebookstore.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.onlinebookstore.dto.category.CategoryRequestDto;
import org.project.onlinebookstore.dto.category.CategoryResponseDto;
import org.project.onlinebookstore.exception.EntityNotFoundException;
import org.project.onlinebookstore.mapper.CategoryMapper;
import org.project.onlinebookstore.model.book.Category;
import org.project.onlinebookstore.repository.category.CategoryRepository;
import org.project.onlinebookstore.service.impl.CategoryServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("""
            findAll method valid case should return
            a Page of CategoryResponseDto
            """)
    public void findAll_ValidCase_ShouldReturnPageOfCategoryResponseDto() {
        // Given
        Long id = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Category category = new Category().setId(id);
        CategoryResponseDto mappedToDtoCategory = new CategoryResponseDto(
                id, null, null
        );

        when(categoryRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(category)));
        when(categoryMapper.toDto(category)).thenReturn(mappedToDtoCategory);

        // When
        Page<CategoryResponseDto> actual = categoryService.findAll(pageable);

        // Then
        assertThat(actual.getContent()).hasSize(1);
        assertThat(actual.getContent().get(0).id()).isEqualTo(id);

        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryMapper);

        verify(categoryRepository).findAll(pageable);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("""
            findById method with id of existing Category
            should return CategoryResponseDto
            """)
    public void findById_WithValidId_ShouldReturnCategoryResponseDto() {
        // Given
        Long id = 1L;
        Category category = new Category().setId(id);
        CategoryResponseDto mappedToDtoCategory = new CategoryResponseDto(
                id, null, null
        );

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(mappedToDtoCategory);

        // When
        CategoryResponseDto actual = categoryService.findById(id);

        // Then
        assertThat(actual.id()).isEqualTo(id);

        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryMapper);

        verify(categoryRepository).findById(id);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("""
            findById method with id of non-existing Category
            should throw EntityNotFoundException
            """)
    public void findById_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidId = 404L;
        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> categoryService.findById(invalidId))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("Cannot find a category by id: " + invalidId);

        // Then
        verify(categoryRepository).findById(invalidId);
        verifyNoMoreInteractions(categoryRepository);

        verifyNoInteractions(categoryMapper);
    }

    @Test
    @DisplayName("""
            save method should return saved Category
            in form of CategoryResponseDto
            """)
    public void save_WithValidRequest_ShouldReturnCategoryResponseDto() {
        // Given
        Long id = 1L;

        CategoryRequestDto createRequestDto = new CategoryRequestDto(
                "Fantasy", null
        );
        Category mappedToModelCategory = new Category().setName("Fantasy");

        Category savedCategory = new Category()
                .setId(id)
                .setName("Fantasy");

        CategoryResponseDto mappedToDtoCategory = new CategoryResponseDto(
                id, "Fantasy", null
        );

        when(categoryRepository.save(mappedToModelCategory)).thenReturn(savedCategory);

        when(categoryMapper.toModel(createRequestDto)).thenReturn(mappedToModelCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(mappedToDtoCategory);

        // When
        CategoryResponseDto actual = categoryService.save(createRequestDto);

        // Then
        assertThat(actual).isEqualTo(mappedToDtoCategory);

        verify(categoryMapper).toModel(createRequestDto);
        verify(categoryMapper).toDto(savedCategory);
        verifyNoMoreInteractions(categoryMapper);

        verify(categoryRepository).save(mappedToModelCategory);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("""
            update method should return CategoryResponseDto
            from Category that was updated by id
            """)
    public void update_WithValidId_ShouldReturnCategoryResponseDto() {
        // Given
        Long id = 1L;
        String updatedName = "Fantasy";

        Category notUpdatedCategory = new Category().setId(id).setName("Fantasie");
        CategoryRequestDto updateRequestDto = new CategoryRequestDto(
                updatedName, null
        );
        Category updatedCategory = new Category().setId(id).setName(updatedName);
        CategoryResponseDto updatedCategoryResponseDto = new CategoryResponseDto(
                id, updatedName, null
        );

        when(categoryRepository.findById(id)).thenReturn(Optional.of(notUpdatedCategory));
        when(categoryRepository.save(notUpdatedCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(updatedCategoryResponseDto);

        // When
        CategoryResponseDto actual = categoryService.update(id, updateRequestDto);

        // Then
        assertThat(actual).isEqualTo(updatedCategoryResponseDto);

        verify(categoryRepository).findById(id);
        verify(categoryRepository).save(notUpdatedCategory);
        verifyNoMoreInteractions(categoryRepository);

        verify(categoryMapper).updateFromDto(notUpdatedCategory, updateRequestDto);
        verify(categoryMapper).toDto(updatedCategory);
        verifyNoMoreInteractions(categoryMapper);
    }

    @Test
    @DisplayName("""
            update method with id of non-existing
            Category should throw EntityNotFoundException
            """)
    public void update_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidId = 404L;
        CategoryRequestDto updateRequestDto = new CategoryRequestDto("test", null);

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> categoryService.update(invalidId, updateRequestDto))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("Cannot find a category by id: " + invalidId);

        // Then
        verify(categoryRepository).findById(invalidId);
        verifyNoMoreInteractions(categoryRepository);

        verifyNoInteractions(categoryMapper);
    }

    @Test
    @DisplayName("""
            deleteById method should soft delete
            existing Category by id
            """)
    public void deleteById_WithValidId_ShouldSoftDeleteCategory() {
        // Given
        Long id = 1L;

        when(categoryRepository.existsById(id)).thenReturn(true);

        // When
        categoryService.deleteById(id);

        // Then
        verify(categoryRepository).existsById(id);
        verify(categoryRepository).deleteById(id);

        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("""
            deleteById method with id of
            non-existing Category should
            throw EntityNotFoundException
            """)
    public void deleteById_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidId = 404L;

        when(categoryRepository.existsById(invalidId)).thenReturn(false);

        // When
        assertThatThrownBy(() -> categoryService.deleteById(invalidId))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("Cannot find a category by id: " + invalidId);

        // Then
        verify(categoryRepository).existsById(invalidId);
        verifyNoMoreInteractions(categoryRepository);
    }
}
