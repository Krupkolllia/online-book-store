package org.project.onlinebookstore.dto.mapper;

import org.mapstruct.Mapper;
import org.project.onlinebookstore.config.MapStructConfig;
import org.project.onlinebookstore.dto.BookDto;
import org.project.onlinebookstore.dto.CreateBookRequestDto;
import org.project.onlinebookstore.model.Book;

@Mapper(config = MapStructConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto dto);
}
