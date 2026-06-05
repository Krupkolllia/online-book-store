package org.project.onlinebookstore.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.onlinebookstore.dto.book.BookResponseDto;
import org.project.onlinebookstore.dto.book.CreateBookRequestDto;
import org.project.onlinebookstore.util.TestDataHelper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.project.onlinebookstore.util.TestDataHelper.ADD_SCRIPT_PATH;
import static org.project.onlinebookstore.util.TestDataHelper.DELETE_SCRIPT_PATH;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookControllerTest extends AbstractControllerTest {

    @Test
    @WithMockUser(username = "user")
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            getAll method should return to client
            a Page of BookResponseDto
            """)
    public void getAll_ValidCase_Success() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        List<BookResponseDto> expected = TestDataHelper.createBookResponseDtoList();

        // When
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .param("page", String.valueOf(pageable.getPageNumber()))
                                .param("size", String.valueOf(pageable.getPageSize()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String content = objectMapper
                .readTree(result.getResponse().getContentAsString())
                .get("content")
                .toString();

        BookResponseDto[] actual = objectMapper.readValue(content, BookResponseDto[].class);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @WithMockUser(username = "user")
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            getBookById method with id of existing Book
            should return a BookResponseDto by this id
            """)
    public void getBookById_WithValidId_ShouldReturnBookResponseDto() throws Exception {
        // Given
        BookResponseDto expected = TestDataHelper.createBookResponseDtoList().get(0);
        Long id = expected.id();

        // When
        MvcResult result = mockMvc.perform(
                        get("/books/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "user")
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            search method should return a Page of Books
            that satisfy search parameters
            """)
    public void search_ValidCase_ShouldReturnPageOfBooks() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        BookResponseDto expected = TestDataHelper.createBookResponseDtoList().get(0);

        // When
        MvcResult result = mockMvc.perform(
                get("/books/search")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("titles", "Test title 1")
                        .param("authors", "Test author 1")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String content = objectMapper
                .readTree(result.getResponse().getContentAsString())
                .get("content")
                .toString();

        BookResponseDto[] actual = objectMapper.readValue(content, BookResponseDto[].class);

        assertThat(actual).hasSize(1);
        assertThat(actual[0]).isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("""
            getBookById method with id of non-existing Book
            should return response with 404 code
            """)
    public void getBookById_WithInvalidId_ShouldReturn404Code() throws Exception {
        // Given
        Long invalidId = 404L;

        // When
        mockMvc.perform(
                        get("/books/{id}", invalidId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            createBook method with valid CreateBookRequestDto
            should return created Book in format of BookResponseDto
            and save that Book to the database
            """)
    public void createBook_WithValidRequest_ShouldReturnCreatedBook() throws Exception {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Test title", "Test author", "978-0201633610",
                new BigDecimal("9.99"), null, null,
                List.of(1L, 2L)
        );

        BookResponseDto expected = new BookResponseDto(
                null, "Test title", "Test author",
                "978-0201633610", new BigDecimal("9.99"), null,
                null, Set.of(1L, 2L)
        );

        String requestJson = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        BookResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class);

        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            updateBookById PUT method should update a Book by id in database
            and return updated book in format of BookResponseDto
            """)
    public void updateBookById_WithValidId_ShouldReturnUpdatedBook() throws Exception {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Test title 1 updated", "Test author 1", "978-0134685991",
                new BigDecimal("9.99"), null, null, List.of(1L, 2L)
        );
        BookResponseDto expected = new BookResponseDto(
                1L, "Test title 1 updated", "Test author 1", "978-0134685991",
                new BigDecimal("9.99"), null, null, Set.of(1L, 2L)
        );
        Long id = TestDataHelper.createBookResponseDtoList().get(0).id();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(
                put("/books/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            updateBookById method with id of non-existing Book
            should return response with 404 code
            """)
    public void updateBookById_WithInvalidId_ShouldReturn404Code() throws Exception {
        // Given
        Long invalidId = 404L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Test title 1 updated", "Test author 1", "978-0134685991",
                new BigDecimal("9.99"), null, null, List.of(1L, 2L)
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        mockMvc.perform(
                put("/books/{id}", invalidId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            deleteBookById method should delete a Book by id
            and return response with 204 status code
            """)
    public void deleteBookById_WithValidId_ShouldReturn204Code() throws Exception {
        // Given
        Long id = TestDataHelper.createBookResponseDtoList().get(0).id();

        // When
        mockMvc.perform(
                delete("/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            deleteBookById method with id of non-existing Book
            should return a response with 404 status code
            """)
    public void deleteBookById_WithInvalidId_ShouldReturn404Code() throws Exception {
        Long invalidId = 404L;

        mockMvc.perform(
                delete("/books/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }
}
