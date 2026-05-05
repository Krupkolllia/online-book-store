package org.project.onlinebookstore.repository.book.spec;

import java.math.BigDecimal;
import org.project.onlinebookstore.dto.book.BookSearchParametersDto;
import org.project.onlinebookstore.dto.spec.PriceRange;
import org.project.onlinebookstore.model.Book;
import org.project.onlinebookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookPriceSpecificationProvider implements SpecificationProvider<Book, PriceRange> {
    private static final String FIELD_NAME = "price";

    @Override
    public String getKey() {
        return BookSearchParametersDto.PRICE;
    }

    @Override
    public Specification<Book> getSpecification(PriceRange value) {
        BigDecimal minPrice = value.min();
        BigDecimal maxPrice = value.max();

        return (root, query, cb) -> {
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get(FIELD_NAME), minPrice, maxPrice);
            } else if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get(FIELD_NAME), minPrice);
            } else {
                return cb.lessThanOrEqualTo(root.get(FIELD_NAME), maxPrice);
            }
        };
    }
}
