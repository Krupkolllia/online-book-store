package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.book.BookResponseDto;
import org.project.onlinebookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import org.project.onlinebookstore.dto.book.BookSearchParametersDto;
import org.project.onlinebookstore.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(CreateBookRequestDto bookDto);

    BookResponseDto findById(Long id);

    Page<BookResponseDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId,
                                                                Pageable pageable);

    Page<BookResponseDto> findAll(Pageable pageable);

    Page<BookResponseDto> search(BookSearchParametersDto params, Pageable pageable);

    BookResponseDto update(Long id, CreateBookRequestDto bookRequestDto);

    void deleteById(Long id);
}
