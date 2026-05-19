package org.project.onlinebookstore.mapper;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.project.onlinebookstore.config.MapStructConfig;
import org.project.onlinebookstore.dto.book.BookResponseDto;
import org.project.onlinebookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import org.project.onlinebookstore.dto.book.CreateBookRequestDto;
import org.project.onlinebookstore.model.Book;
import org.project.onlinebookstore.model.Category;

@Mapper(config = MapStructConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", expression = "java(mapCategoryIds(book))")
    BookResponseDto toDto(Book book);

    Book toModel(CreateBookRequestDto dto);

    BookResponseDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(@MappingTarget Book book, CreateBookRequestDto dto);

    default Set<Long> mapCategoryIds(Book book) {
        if (book.getCategories() == null) {
            return Collections.emptySet();
        }
        return book.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
