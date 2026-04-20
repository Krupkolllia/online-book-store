package org.project.onlinebookstore.service;

import java.util.List;
import org.project.onlinebookstore.dto.BookDto;
import org.project.onlinebookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll();
}
