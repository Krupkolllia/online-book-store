package org.project.onlinebookstore.service;

import java.util.List;
import org.project.onlinebookstore.dto.book.BookDto;
import org.project.onlinebookstore.dto.book.CreateBookRequestDto;
import org.project.onlinebookstore.dto.book.UpdateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    BookDto findById(Long id);

    List<BookDto> findAll();

    BookDto update(Long id, UpdateBookRequestDto bookRequestDto);
}
