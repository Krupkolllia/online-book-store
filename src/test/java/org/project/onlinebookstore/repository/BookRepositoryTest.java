package org.project.onlinebookstore.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.onlinebookstore.model.book.Book;
import org.project.onlinebookstore.model.book.Category;
import org.project.onlinebookstore.repository.book.BookRepository;
import org.project.onlinebookstore.util.TestDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.project.onlinebookstore.util.TestDataHelper.ADD_SCRIPT_PATH;
import static org.project.onlinebookstore.util.TestDataHelper.DELETE_SCRIPT_PATH;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            findAll method when db has Books and Categories
            should return the right Page of
            non-empty Books with non-empty categories
            """)
    public void findAll_ValidCase_ShouldReturnPageOfBooks() {
        // Given
        List<Book> expected = TestDataHelper.createBooks();

        Pageable pageable = PageRequest.of(0, 10);

        // When
        List<Book> actual = new ArrayList<>(bookRepository.findAll(pageable).getContent());
        actual.remove(3);

        // Then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("""
        findAll method when called on empty DB
        should return empty Page of Books
        """)
    public void findAll_WithEmptyDB_ShouldReturnEmptyPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> actual = bookRepository.findAll(pageable);

        // Then
        assertThat(actual.getTotalElements()).isZero();
        assertThat(actual.getContent()).isEmpty();
    }

    @Test
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            findAll method with Specification should
            return a Page of Books according to Specification
            """)
    public void findAll_WithSpecification_ShouldReturnPageOfBooks() {
        // Given
        List<Book> expected = List.of(TestDataHelper.createBooks().get(0));

        Specification<Book> spec = (root, query, cb)
                -> cb.equal(root.get("isbn"), "978-0134685991");
        Pageable pageable = PageRequest.of(0, 10);

        // When
        List<Book> actual = bookRepository.findAll(spec, pageable).getContent();

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            findById method with id of existing Book
            should return non-empty Optional of Book
            with non-empty Categories
            """)
    public void findById_WithValidId_ShouldReturnOptionalOfBook() {
        // Given
        Book expected = TestDataHelper.createBooks().get(0);
        Long id = expected.getId();

        // When
        Optional<Book> actual = bookRepository.findById(id);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual).get().isEqualTo(expected);
    }

    @Test
    @DisplayName("""
            findById method with id of not existing Book
            should return an empty Optional of Book
            """)
    public void findById_WithInvalidId_ShouldReturnEmptyOptional() {
        // Given
        Long invalidId = 404L;

        // When
        Optional<Book> actual = bookRepository.findById(invalidId);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            findAllByCategoryId method with id of existing
            Category should return a Page of Books found
            by category id
            """)
    public void findAllByCategoryId_WithValidId_ShouldReturnPageOfBooks() {
        // Given
        Category category = TestDataHelper.createCategories().get(0);
        Long categoryId = category.getId();

        List<Book> books = TestDataHelper.createBooks();
        List<Book> expected = List.of(
                books.get(0),
                books.get(1)
        );

        Pageable pageable = PageRequest.of(0, 10);

        // When
        List<Book> actual = bookRepository.findAllByCategoryId(categoryId, pageable).getContent();

        // Then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("""
            findAllByCategoryId method with id of non-existing
            Category should return an empty Page of Books
            """)
    public void findAllByCategoryId_WithInvalidId_ShouldReturnEmptyPage() {
        // Given
        Long invalidCategoryId = 404L;
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> actual = bookRepository.findAllByCategoryId(invalidCategoryId, pageable);

        // Then
        assertThat(actual.getTotalElements()).isZero();
        assertThat(actual.getContent()).isEmpty();
    }
}
