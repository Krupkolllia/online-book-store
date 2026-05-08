package org.project.onlinebookstore.repository.book.spec;

import jakarta.persistence.criteria.Predicate;
import java.util.List;
import org.project.onlinebookstore.dto.book.BookSearchParametersDto;
import org.project.onlinebookstore.model.Book;
import org.project.onlinebookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookTitleSpecificationProvider implements SpecificationProvider<Book, List<String>> {
    private static final String FIELD_NAME = "title";

    @Override
    public String getKey() {
        return BookSearchParametersDto.TITLES;
    }

    @Override
    public Specification<Book> getSpecification(List<String> value) {
        return (root, query, cb) -> {
            List<Predicate> predicates = value.stream()
                    .map(String::trim)
                    .map(v -> cb.like(
                            cb.lower(root.get(FIELD_NAME)),
                            "%" + v.toLowerCase() + "%"
                    ))
                    .toList();

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
