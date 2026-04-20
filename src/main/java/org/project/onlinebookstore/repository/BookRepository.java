package org.project.onlinebookstore.repository;

import java.util.List;
import org.project.onlinebookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
