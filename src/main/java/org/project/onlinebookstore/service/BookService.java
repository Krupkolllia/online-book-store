package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.book.BookDto;
import org.project.onlinebookstore.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    BookDto findById(Long id);

    Page<BookDto> findAll(Pageable pageable);

    BookDto update(Long id, CreateBookRequestDto bookRequestDto);

    void deleteById(Long id);
}
