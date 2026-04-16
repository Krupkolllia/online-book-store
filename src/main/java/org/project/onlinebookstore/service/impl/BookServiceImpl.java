package org.project.onlinebookstore.service.impl;

import java.util.List;
import org.project.onlinebookstore.model.Book;
import org.project.onlinebookstore.repository.BookRepository;
import org.project.onlinebookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
