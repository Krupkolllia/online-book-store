package org.project.onlinebookstore.repository.book.spec;

import java.util.List;
import org.project.onlinebookstore.dto.book.BookSearchParametersDto;
import org.project.onlinebookstore.model.Book;
import org.project.onlinebookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookCategorySpecificationProvider
        implements SpecificationProvider<Book, List<String>> {
    private static final String JOIN_FIELD_NAME = "categories";
    private static final String FIELD_NAME = "id";

    @Override
    public String getKey() {
        return BookSearchParametersDto.CATEGORY_IDS;
    }

    @Override
    public Specification<Book> getSpecification(List<String> value) {
        return (root, query, cb) -> {
            if (query != null) {
                query.distinct(true);
            }
            return root.join(JOIN_FIELD_NAME).get(FIELD_NAME)
                    .in(value.stream()
                            .map(Long::valueOf)
                            .toList());
        };
    }
}
