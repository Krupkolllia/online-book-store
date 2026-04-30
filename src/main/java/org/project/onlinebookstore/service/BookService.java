package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.book.BookResponseDto;
import org.project.onlinebookstore.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(CreateBookRequestDto book);

    BookResponseDto findById(Long id);

    Page<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto update(Long id, CreateBookRequestDto bookRequestDto);

    void deleteById(Long id);
}
