package org.project.onlinebookstore.repository.book.spec;

import java.util.List;
import org.project.onlinebookstore.dto.book.BookSearchParametersDto;
import org.project.onlinebookstore.model.book.Book;
import org.project.onlinebookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookAuthorSpecificationProvider implements SpecificationProvider<Book, List<String>> {
    private static final String FIELD_NAME = "author";

    @Override
    public String getKey() {
        return BookSearchParametersDto.AUTHORS;
    }

    @Override
    public Specification<Book> getSpecification(List<String> value) {
        return (root, query, cb) -> root.get(FIELD_NAME).in(value);
    }
}
