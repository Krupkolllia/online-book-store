package org.project.onlinebookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.book.BookResponseDto;
import org.project.onlinebookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import org.project.onlinebookstore.dto.book.BookSearchParametersDto;
import org.project.onlinebookstore.dto.book.CreateBookRequestDto;
import org.project.onlinebookstore.exception.EntityNotFoundException;
import org.project.onlinebookstore.mapper.BookMapper;
import org.project.onlinebookstore.model.book.Book;
import org.project.onlinebookstore.repository.book.BookRepository;
import org.project.onlinebookstore.repository.book.spec.BookSpecificationBuilder;
import org.project.onlinebookstore.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookSpecificationBuilder bookSpecificationBuilder;

    private final BookMapper bookMapper;

    @Override
    public BookResponseDto save(CreateBookRequestDto bookDto) {
        Book book = bookMapper.toModel(bookDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    @Override
    public BookResponseDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find a book by id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BookResponseDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId,
                                                                       Pageable pageable) {
        return bookRepository.findAllByCategoryId(categoryId, pageable)
                .map(bookMapper::toDtoWithoutCategories);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::toDto);

    }

    @Transactional(readOnly = true)
    @Override
    public Page<BookResponseDto> search(BookSearchParametersDto params, Pageable pageable) {
        return bookRepository.findAll(bookSpecificationBuilder.build(params), pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookResponseDto update(Long id, CreateBookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find a book by id: " + id)
        );
        bookMapper.updateFromDto(book, bookRequestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot find a book by id: " + id);
        }
        bookRepository.deleteById(id);
    }
}
