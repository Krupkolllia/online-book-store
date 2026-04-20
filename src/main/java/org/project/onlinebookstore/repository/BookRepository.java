package org.project.onlinebookstore.repository;

import java.util.List;
import java.util.Optional;

import org.project.onlinebookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> findById(Long id);

    List<Book> findAll();
}
