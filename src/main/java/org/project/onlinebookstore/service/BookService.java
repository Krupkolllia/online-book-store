package org.project.onlinebookstore.service;

import java.util.List;
import org.project.onlinebookstore.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
