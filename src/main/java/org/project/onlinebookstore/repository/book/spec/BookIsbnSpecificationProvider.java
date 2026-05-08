package org.project.onlinebookstore.repository.book.spec;

import org.project.onlinebookstore.dto.book.BookSearchParametersDto;
import org.project.onlinebookstore.model.Book;
import org.project.onlinebookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookIsbnSpecificationProvider implements SpecificationProvider<Book, String> {
    private static final String FIELD_NAME = "isbn";

    @Override
    public String getKey() {
        return BookSearchParametersDto.ISBN;
    }

    @Override
    public Specification<Book> getSpecification(String value) {
        return (root, query, cb) -> cb.equal(root.get(FIELD_NAME), value);

    }
}
