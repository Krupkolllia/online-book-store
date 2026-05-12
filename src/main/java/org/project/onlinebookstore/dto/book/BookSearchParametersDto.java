package org.project.onlinebookstore.dto.book;

import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.List;
import org.project.onlinebookstore.exception.validator.isbn.Isbn;

public record BookSearchParametersDto(
        List<String> titles,
        List<String> authors,
        List<Long> categoryIds,
        @Isbn
        String isbn,
        @DecimalMin(value = "0.0", message = "Min price must be >= 0")
        BigDecimal minPrice,
        @DecimalMin(value = "0.0", message = "Max price must be >= 0")
        BigDecimal maxPrice
) {

    public static final String TITLES = "titles";
    public static final String AUTHORS = "authors";
    public static final String CATEGORY_IDS = "categoryIds";
    public static final String ISBN = "isbn";
    public static final String PRICE = "price";
}
