package org.project.onlinebookstore.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.onlinebookstore.dto.book.BookResponseDto;
import org.project.onlinebookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import org.project.onlinebookstore.dto.book.BookSearchParametersDto;
import org.project.onlinebookstore.dto.book.CreateBookRequestDto;
import org.project.onlinebookstore.exception.EntityNotFoundException;
import org.project.onlinebookstore.mapper.BookMapper;
import org.project.onlinebookstore.model.book.Book;
import org.project.onlinebookstore.model.book.Category;
import org.project.onlinebookstore.repository.book.BookRepository;
import org.project.onlinebookstore.repository.book.spec.BookSpecificationBuilder;
import org.project.onlinebookstore.service.impl.BookServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    BookSpecificationBuilder specificationBuilder;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("""
            Save method with valid request dto returns response dto
            """)
    public void save_WithValidRequest_ShouldReturnBookResponseDto() {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Clean code", "Robert C. Martin", "978-0135398548",
                BigDecimal.TEN, null, null, List.of(1L, 2L)
        );

        Book mappedToModelBook = new Book();
        Book savedBook = new Book().setId(1L);
        BookResponseDto mappedToResponseDtoBook = new BookResponseDto(1L,
                null, null, null, null, null, null, null);
        when(bookRepository.save(mappedToModelBook)).thenReturn(savedBook);
        when(bookMapper.toModel(requestDto)).thenReturn(mappedToModelBook);
        when(bookMapper.toDto(savedBook)).thenReturn(mappedToResponseDtoBook);

        // When
        BookResponseDto actual = bookService.save(requestDto);

        // Then
        assertEquals(1L, actual.id());

        verify(bookMapper).toModel(requestDto);
        verify(bookMapper).toDto(savedBook);
        verifyNoMoreInteractions(bookMapper);

        verify(bookRepository).save(mappedToModelBook);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            findById method should return BookResponseDto if
            Book with given id exists
            """)
    public void findById_WithValidId_ShouldReturnBookResponseDto() {
        // Given
        Long id = 1L;
        Book foundBook = new Book().setId(id);
        BookResponseDto mappedToDtoBook = new BookResponseDto(id,
                null, null, null, null, null, null, null);
        when(bookRepository.findById(id)).thenReturn(Optional.of(foundBook));
        when(bookMapper.toDto(foundBook)).thenReturn(mappedToDtoBook);

        // When
        BookResponseDto actual = bookService.findById(id);

        // Then
        assertEquals(id, actual.id());

        verify(bookMapper).toDto(foundBook);
        verifyNoMoreInteractions(bookMapper);

        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            findById method should throw EntityNotFoundException
            if Book with given id not exists
            """)
    public void findById_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidId = 404L;
        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(invalidId)
        );

        // Then
        String expected = "Cannot find a book by id: " + invalidId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(bookRepository).findById(invalidId);
        verifyNoMoreInteractions(bookRepository);
    }



    @Test
    @DisplayName("""
            findAllByCategoryId with existing category id should
            return page of BookResponseDtoWithoutCategoryIds
            """)
    public void findAllByCategoryId_ValidCategoryId_ReturnsPage() {
        // Given
        Long bookId = 1L;
        Long categoryId = 2L;
        Book book = new Book()
                .setId(bookId)
                .setCategories(Set.of(new Category().setId(categoryId)));
        PageRequest pageRequest = PageRequest.of(0, 10);
        var bookResponseDtoWithoutCategoryIds = new BookResponseDtoWithoutCategoryIds(
                bookId, null, null, null, null, null, null
        );
        when(bookRepository.findAllByCategoryId(categoryId, pageRequest))
                .thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toDtoWithoutCategories(book))
                .thenReturn(bookResponseDtoWithoutCategoryIds);

        // When
        Page<BookResponseDtoWithoutCategoryIds> actual =
                bookService.findAllByCategoryId(categoryId, pageRequest);

        // Then
        assertEquals(1, actual.getContent().size());
        assertEquals(bookId, actual.getContent().get(0).id());

        verify(bookMapper).toDtoWithoutCategories(book);
        verifyNoMoreInteractions(bookMapper);

        verify(bookRepository).findAllByCategoryId(categoryId, pageRequest);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
        findAll method in valid case should return Page<BookResponseDto>
        """)
    public void findAll_ValidCase_ShouldReturnPageOfBookResponseDto() {
        // Given
        Long id = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10);
        Book book = new Book().setId(id);
        BookResponseDto mappedToDtoBook = new BookResponseDto(id,
                null, null, null, null, null, null, null);
        when(bookRepository.findAll(pageRequest))
                .thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toDto(book)).thenReturn(mappedToDtoBook);

        // When
        Page<BookResponseDto> actual = bookService.findAll(pageRequest);

        // Then
        assertEquals(1, actual.getContent().size());
        assertEquals(id, actual.getContent().get(0).id());

        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookMapper);

        verify(bookRepository).findAll(pageRequest);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            update method should update requested fields
            in Book to DB and return updated Book in form of BookResponseDto
            """)
    public void update_WithValidId_ShouldReturnUpdatedBookResponseDto() {
        // Given
        Long bookId = 1L;
        Long categoryId = 2L;
        String notUpdatedTitle = "Clean cod";
        String updatedTitle = "Clean code";
        Book notUpdatedBook = new Book()
                .setId(bookId).setTitle(notUpdatedTitle)
                .setAuthor("Robert C. Martin").setIsbn("978-0135398548")
                .setPrice(BigDecimal.valueOf(100))
                .setDescription(null).setCoverImage(null)
                .setCategories(Set.of(new Category().setId(categoryId)));

        CreateBookRequestDto updateBookRequestDto = new CreateBookRequestDto(
                updatedTitle, "Robert C. Martin", "978-0135398548",
                BigDecimal.valueOf(100), null, null, List.of(1L, 2L)
        );

        Book updatedBook = new Book().setId(bookId).setTitle(updatedTitle);
        BookResponseDto updatedBookResponseDto = new BookResponseDto(
                bookId, updatedTitle, null, null, null, null, null, null
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(notUpdatedBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        when(bookMapper.toDto(any(Book.class))).thenReturn(updatedBookResponseDto);

        // When
        BookResponseDto actual = bookService.update(bookId, updateBookRequestDto);

        // Then
        assertEquals(updatedBook.getId(), actual.id());
        assertEquals(updatedTitle, actual.title());

        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(any(Book.class));
        verifyNoMoreInteractions(bookRepository);

        verify(bookMapper).toDto(any(Book.class));
        verify(bookMapper).updateFromDto(notUpdatedBook, updateBookRequestDto);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("""
            update method should throw EntityNotFoundException
            if Book with given id not exists
            """)
    public void update_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidId = 404L;
        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Clean code", "Robert C. Martin", "978-0135398548",
                BigDecimal.valueOf(100), null, null, List.of(1L, 2L)
        );

        // When
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.update(invalidId, requestDto)
        );

        // Then
        String expected = "Cannot find a book by id: " + invalidId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(bookRepository).findById(invalidId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            deleteById method should update isDeleted field
            to true (soft delete) if Book with given id exists
            """)
    public void deleteById_WithValidId_SoftDeletesGivenBook() {
        // Given
        Long id = 1L;
        when(bookRepository.existsById(id)).thenReturn(true);

        // When
        bookService.deleteById(id);

        // Then
        verify(bookRepository).existsById(id);
        verify(bookRepository).deleteById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            update method should throw EntityNotFoundException
            if Book with given id not exists
            """)
    public void deleteById_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidId = 404L;
        when(bookRepository.existsById(invalidId)).thenReturn(false);

        // When
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.deleteById(invalidId)
        );

        // Then
        String expected = "Cannot find a book by id: " + invalidId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(bookRepository).existsById(invalidId);
        verify(bookRepository, never()).deleteById(invalidId);
        verifyNoMoreInteractions(bookRepository);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("""
            search method with valid search params should
            return the Page of BookResponseDto that satisfy search params
            """)
    public void search_WithValidSearchParams_ShouldReturnPage() {
        // Given
        Long id = 1L;
        String searchedIsbn = "978-0135398548";

        Book searchedBook = new Book()
                .setId(id)
                .setIsbn(searchedIsbn);

        BookResponseDto searchedBookResponseDto = new BookResponseDto(
                id, null, null, searchedIsbn, null, null, null, null
        );

        BookSearchParametersDto searchParams = new BookSearchParametersDto(
                null, null, null, searchedIsbn, null, null
        );

        PageRequest pageRequest = PageRequest.of(0, 10);

        when(specificationBuilder.build(any(BookSearchParametersDto.class)))
                .thenReturn(mock(Specification.class));

        when(bookRepository.findAll(any(Specification.class), eq(pageRequest)))
                .thenReturn(new PageImpl<>(List.of(searchedBook)));

        when(bookMapper.toDto(searchedBook)).thenReturn(searchedBookResponseDto);

        // When
        Page<BookResponseDto> actual = bookService.search(searchParams, pageRequest);

        // Then
        assertEquals(1, actual.getContent().size());

        BookResponseDto dto = actual.getContent().get(0);
        assertEquals(id, dto.id());
        assertEquals(searchedIsbn, dto.isbn());

        verify(bookRepository).findAll(any(Specification.class), eq(pageRequest));
        verifyNoMoreInteractions(bookRepository);

        verify(bookMapper).toDto(searchedBook);
        verifyNoMoreInteractions(bookMapper);
    }
}
