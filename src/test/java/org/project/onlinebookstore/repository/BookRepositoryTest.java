package org.project.onlinebookstore.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.onlinebookstore.model.book.Book;
import org.project.onlinebookstore.model.book.Category;
import org.project.onlinebookstore.repository.book.BookRepository;
import org.project.onlinebookstore.repository.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("""
            findAll method when db has Books and Categories
            should return the right Page of
            non-empty Books with non-empty categories
            """)
    public void findAll_ValidCase_ShouldReturnPageOfBooks() {
        // Given
        createAndSaveBooks(createAndSaveCategories());

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> actual = bookRepository.findAll(pageable);

        // Then
        assertThat(actual.getTotalElements()).isEqualTo(2);
        assertThat(actual.getContent()).hasSize(2);
        assertThat(actual.getContent().get(0).getCategories()).isNotEmpty();
        assertThat(actual.getContent().get(1).getCategories()).isNotEmpty();
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
    @DisplayName("""
            findAll method with Specification should
            return a Page of Books according to Specification
            """)
    public void findAll_WithSpecification_ShouldReturnPageOfBooks() {
        // Given
        createAndSaveBooks(createAndSaveCategories());

        Specification<Book> spec = (root, query, cb)
                -> cb.equal(root.get("isbn"), "000-0000000000");
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> actual = bookRepository.findAll(spec, pageable);

        // Then
        assertThat(actual.getTotalElements()).isEqualTo(1);
        assertThat(actual.getContent()).hasSize(1);
        assertThat(actual.getContent().get(0).getCategories()).isNotEmpty();
        assertThat(actual.getContent().get(0).getIsbn()).isEqualTo("000-0000000000");
    }

    @Test
    @DisplayName("""
            findById method with id of existing Book
            should return non-empty Optional of Book
            with non-empty Categories
            """)
    public void findById_WithValidId_ShouldReturnOptionalOfBook() {
        // Given
        List<Category> categories = createAndSaveCategories();
        Book book1 = createBook("999-9999999999", Set.of(categories.get(0)));
        Book book2 = createBook("000-0000000000", Set.of(categories.get(1)));
        bookRepository.save(book1);
        bookRepository.save(book2);

        Long requestedId = book1.getId();

        // When
        Optional<Book> actual = bookRepository.findById(requestedId);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().getId()).isEqualTo(requestedId);
        assertThat(actual.get().getIsbn()).isEqualTo(book1.getIsbn());
        assertThat(actual.get().getCategories()).isNotEmpty();
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
    @DisplayName("""
            findAllByCategoryId method with id of existing
            Category should return a Page of Books found
            by category id
            """)
    public void findAllByCategoryId_WithValidId_ShouldReturnPageOfBooks() {
        // Given
        List<Category> categories = createAndSaveCategories();
        List<Book> books = createAndSaveBooks(categories);

        Long categoryId = categories.get(0).getId();

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> actual = bookRepository.findAllByCategoryId(categoryId, pageable);

        // Then
        assertThat(actual.getTotalElements()).isEqualTo(1);
        assertThat(actual.getContent()).hasSize(1);
        assertThat(actual.getContent().get(0).getCategories())
                .containsOnly(categories.get(0));
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



    private Book createBook(String isbn, Set<Category> categories) {
        return new Book()
                .setTitle("Test title")
                .setAuthor("Test author")
                .setIsbn(isbn)
                .setPrice(BigDecimal.TEN)
                .setCategories(categories);
    }

    private List<Category> createAndSaveCategories() {
        Category category1 = new Category().setName("Fantasy");
        Category category2 = new Category().setName("Horror");
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        return List.of(category1, category2);
    }

    private List<Book> createAndSaveBooks(List<Category> categories) {
        Book book1 = createBook("999-9999999999", Set.of(categories.get(0)));
        Book book2 = createBook("000-0000000000", Set.of(categories.get(1)));
        bookRepository.save(book1);
        bookRepository.save(book2);

        return List.of(book1, book2);
    }
}
