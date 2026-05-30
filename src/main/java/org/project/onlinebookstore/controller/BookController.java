package org.project.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.book.BookResponseDto;
import org.project.onlinebookstore.dto.book.BookSearchParametersDto;
import org.project.onlinebookstore.dto.book.CreateBookRequestDto;
import org.project.onlinebookstore.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Books API", description = "Operations related to book catalog management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Get all books")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<BookResponseDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Search books by parameters")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search")
    public Page<BookResponseDto> search(@ModelAttribute @Valid BookSearchParametersDto params,
                                        Pageable pageable) {
        return bookService.search(params, pageable);
    }

    @Operation(summary = "Get a book by id")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @Operation(summary = "Add book to the database")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookResponseDto createBook(@RequestBody @Valid CreateBookRequestDto bookRequestDto) {
        return bookService.save(bookRequestDto);
    }

    @Operation(summary = "Update a book (PUT) by id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public BookResponseDto updateBookById(@PathVariable Long id,
                                          @RequestBody @Valid CreateBookRequestDto bookRequestDto) {
        return bookService.update(id, bookRequestDto);
    }

    @Operation(summary = "Delete a book by id")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
