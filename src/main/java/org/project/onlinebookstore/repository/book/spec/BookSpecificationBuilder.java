package org.project.onlinebookstore.repository.book.spec;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.book.BookSearchParametersDto;
import org.project.onlinebookstore.dto.spec.PriceRange;
import org.project.onlinebookstore.model.Book;
import org.project.onlinebookstore.repository.SpecificationBuilder;
import org.project.onlinebookstore.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements
        SpecificationBuilder<Book, BookSearchParametersDto> {
    private final SpecificationProviderManager<Book> specProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);

        if (notEmpty(searchParameters.titles())) {
            spec = spec.and(specProviderManager.getSpecificationProvider(
                    BookSearchParametersDto.TITLES).getSpecification(searchParameters.titles()));
        }

        if (notEmpty(searchParameters.authors())) {
            spec = spec.and(specProviderManager.getSpecificationProvider(
                    BookSearchParametersDto.AUTHORS).getSpecification(searchParameters.authors()));
        }

        if (notBlank(searchParameters.isbn())) {
            spec = spec.and(specProviderManager.getSpecificationProvider(
                    BookSearchParametersDto.ISBN).getSpecification(searchParameters.isbn()));
        }

        if (hasPrice(searchParameters)) {
            spec = spec.and(specProviderManager.getSpecificationProvider(
                    BookSearchParametersDto.PRICE).getSpecification(
                            new PriceRange(searchParameters.minPrice(), searchParameters.maxPrice()
                    )
            ));
        }

        if (notEmpty(searchParameters.categoryIds())) {
            spec = spec.and(specProviderManager.getSpecificationProvider(
                    BookSearchParametersDto.CATEGORY_IDS
            ).getSpecification(searchParameters.categoryIds()));
        }

        return spec;
    }

    private boolean notEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();

    }

    private boolean hasPrice(BookSearchParametersDto searchParameters) {
        return searchParameters.minPrice() != null || searchParameters.maxPrice() != null;
    }
}
