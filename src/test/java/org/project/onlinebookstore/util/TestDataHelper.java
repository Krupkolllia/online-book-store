package org.project.onlinebookstore.util;

import org.project.onlinebookstore.dto.book.BookResponseDto;
import org.project.onlinebookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import org.project.onlinebookstore.dto.category.CategoryResponseDto;
import org.project.onlinebookstore.model.book.Book;
import org.project.onlinebookstore.model.book.Category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestDataHelper {

    public static List<BookResponseDto> createBookResponseDtoList() {
        List<BookResponseDto> responseDtoList = new ArrayList<>();
        responseDtoList.add(new BookResponseDto(
                1L, "Test title 1", "Test author 1", "978-0134685991",
                new BigDecimal("9.99"), null, null, Set.of(1L, 2L)
        ));
        responseDtoList.add(new BookResponseDto(
                2L, "Test title 2", "Test author 2", "978-0132350884",
                new BigDecimal("9.99"), null, null, Set.of(1L, 3L)
        ));
        responseDtoList.add(new BookResponseDto(
                3L, "Test title 3", "Test author 3", "978-0131103627",
                new BigDecimal("9.99"), null, null, Set.of(2L, 3L)
        ));

        return responseDtoList;
    }

    public static List<BookResponseDtoWithoutCategoryIds> createBookResponseDtoWithoutCategoryIdsList() {
        List<BookResponseDtoWithoutCategoryIds> responseDtoList = new ArrayList<>();

        responseDtoList.add(new BookResponseDtoWithoutCategoryIds(
                1L, "Test title 1", "Test author 1", "978-0134685991",
                new BigDecimal("9.99"), null, null
        ));
        responseDtoList.add(new BookResponseDtoWithoutCategoryIds(
                2L, "Test title 2", "Test author 2", "978-0132350884",
                new BigDecimal("9.99"), null, null
        ));
        responseDtoList.add(new BookResponseDtoWithoutCategoryIds(
                3L, "Test title 3", "Test author 3", "978-0131103627",
                new BigDecimal("9.99"), null, null
        ));

        return responseDtoList;
    }

    public static List<CategoryResponseDto> createCategoryResponseDtoList() {
        List<CategoryResponseDto> responseDtoList = new ArrayList<>();
        responseDtoList.add(new CategoryResponseDto(1L, "Fantasy", null));
        responseDtoList.add(new CategoryResponseDto(2L, "Horror", null));
        responseDtoList.add(new CategoryResponseDto(3L, "History", null));

        return responseDtoList;
    }

    public static Book createBook(Long id, String isbn, Set<Category> categories) {
        return new Book()
                .setId(id)
                .setTitle("Test title " + id)
                .setAuthor("Test author " + id)
                .setIsbn(isbn)
                .setPrice(new BigDecimal("9.99"))
                .setCategories(categories);
    }

    public static List<Category> createCategories() {
        Category category1 = new Category().setId(1L).setName("Fantasy");
        Category category2 = new Category().setId(2L).setName("Horror");
        Category category3 = new Category().setId(3L).setName("History");

        return List.of(category1, category2, category3);
    }

    public static List<Book> createBooks() {
        List<Category> categories = createCategories();

        Book book1 = createBook(
                1L, "978-0134685991", Set.of(categories.get(0), categories.get(1)));
        Book book2 = createBook(
                2L, "978-0132350884", Set.of(categories.get(0), categories.get(2)));
        Book book3 = createBook(
                3L, "978-0131103627", Set.of(categories.get(1), categories.get(2)));

        return List.of(book1, book2, book3);
    }
}
