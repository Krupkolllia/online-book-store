package org.project.onlinebookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.model.Book;
import org.project.onlinebookstore.repository.BookRepository;
import org.project.onlinebookstore.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
