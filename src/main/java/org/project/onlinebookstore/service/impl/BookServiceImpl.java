package org.project.onlinebookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.book.BookDto;
import org.project.onlinebookstore.dto.book.CreateBookRequestDto;
import org.project.onlinebookstore.dto.book.UpdateBookRequestDto;
import org.project.onlinebookstore.dto.mapper.BookMapper;
import org.project.onlinebookstore.exception.EntityNotFoundException;
import org.project.onlinebookstore.model.Book;
import org.project.onlinebookstore.repository.BookRepository;
import org.project.onlinebookstore.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book book = bookMapper.toModel(bookRequestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find a book by id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto update(Long id, UpdateBookRequestDto bookRequestDto) {
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
