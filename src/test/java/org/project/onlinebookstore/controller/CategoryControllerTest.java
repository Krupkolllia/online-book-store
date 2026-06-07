package org.project.onlinebookstore.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.onlinebookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import org.project.onlinebookstore.dto.category.CategoryRequestDto;
import org.project.onlinebookstore.dto.category.CategoryResponseDto;
import org.project.onlinebookstore.util.TestDataHelper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

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

public class CategoryControllerTest extends AbstractControllerTest {

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            createCategory method with valid CategoryRequestDto
            should return a created Category in format of
            CategoryResponseDto
            """)
    public void createCategory_ValidCase_ShouldReturnCategoryResponseDto() throws Exception {
        // Given
        String categoryName = "Fantasy";

        CategoryRequestDto createRequestDto = new CategoryRequestDto(
                categoryName, null);
        CategoryResponseDto expected = new CategoryResponseDto(
                null, categoryName, null);

        String requestJson = objectMapper.writeValueAsString(createRequestDto);

        // When
        MvcResult result = mockMvc.perform(
                post("/categories")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);

        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "user")
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            getAll method should return
            a Page of Categories
            """)
    public void getAll_ValidCase_ShouldReturnPageOfCategories() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<CategoryResponseDto> expected = TestDataHelper.createCategoryResponseDtoList();

        // When
        MvcResult result = mockMvc.perform(
                get("/categories")
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

        CategoryResponseDto[] actual = objectMapper.readValue(content, CategoryResponseDto[].class);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @WithMockUser(username = "user")
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            getCategoryById method with id of existing Category
            should return CategoryResponseDto by its id
            """)
    public void getCategoryById_WithValidId_ShouldReturnCategoryResponseDto() throws Exception {
        // Given
        CategoryResponseDto expected = TestDataHelper.createCategoryResponseDtoList().get(0);
        Long id = expected.id();

        // When
        MvcResult result = mockMvc.perform(
                get("/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("""
            getCategoryById method with id of non-existing
            Category should return response with 404 status code
            """)
    public void getCategoryById_WithInvalidId_ShouldReturn404StatusCode() throws Exception {
        // Given
        Long invalidId = 404L;

        // When & Then
        mockMvc.perform(
                get("/categories/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            updateCategory PUT method with id of existing Category
            should return updated Category by id
            in format of CategoryResponseDto
            """)
    public void updateCategory_WithValidId_ShouldReturnUpdatedCategory() throws Exception {
        // Given
        Long id = TestDataHelper.createCategoryResponseDtoList().get(0).id();
        String updatedName = "Fantasy updated";

        CategoryRequestDto updateRequestDto = new CategoryRequestDto(
                updatedName, null
        );

        CategoryResponseDto expected = new CategoryResponseDto(
                id, updatedName, null
        );

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        // When
        MvcResult result = mockMvc.perform(
                put("/categories/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class
        );

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            updateCategory PUT method with id of non-existing Category
            should return response with 404 status code
            """)
    public void updateCategory_WithInvalidId_ShouldReturn404StatusCode() throws Exception {
        // Given
        Long invalidId = 404L;
        CategoryRequestDto updateRequestDto = new CategoryRequestDto(
                "Fantasy", null
        );

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        // When & Then
        mockMvc.perform(
                put("/categories/{id}", invalidId)
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
            deleteCategory method with id of existing Category
            should delete a Category by id and
            return response with 204 status code
            """)
    public void deleteCategory_WithValidId_ShouldReturn204StatusCode() throws Exception {
        // Given
        Long id = TestDataHelper.createCategoryResponseDtoList().get(0).id();

        // When & Then
        mockMvc.perform(
                delete("/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            deleteCategory method with id of non-existing Category
            should return response with status code 404
            """)
    public void deleteCategory_WithInvalidId_ShouldReturn404StatusCode() throws Exception {
        // Given
        Long invalidId = 404L;

        // When & Then
        mockMvc.perform(
                delete("/categories/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user")
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
        getAllByCategoryId method with existing category id
        should return not empty Page of BookResponseDtoWithoutCategoryIds
        """)
    public void getAllByCategoryId_WithValidId_ShouldReturnPageOfBooks() throws Exception {
        // Given
        Long id = TestDataHelper.createCategoryResponseDtoList().get(0).id();
        List<BookResponseDtoWithoutCategoryIds> all = TestDataHelper
                .createBookResponseDtoWithoutCategoryIdsList();
        List<BookResponseDtoWithoutCategoryIds> expected = List.of(all.get(0), all.get(1));

        Pageable pageable = PageRequest.of(0, 10);

        // When
        MvcResult result = mockMvc.perform(
                get("/categories/{id}/books", id)
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

        BookResponseDtoWithoutCategoryIds[] actual = objectMapper
                .readValue(content, BookResponseDtoWithoutCategoryIds[].class);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("""
            getAllByCategoryId method with id of non-existing Category
            should return empty Page of BookResponseDtoWithoutCategoryIds
            """)
    public void getAllByCategoryId_WithInvalidId_ShouldReturnEmptyPageOfBooks() throws Exception {
        // Given
        Long invalidCategoryId = 404L;
        List<BookResponseDtoWithoutCategoryIds> expected = List.of();

        Pageable pageable = PageRequest.of(0, 10);

        // When
        MvcResult result = mockMvc.perform(
                get("/categories/{id}/books", invalidCategoryId)
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

        BookResponseDtoWithoutCategoryIds[] actual = objectMapper
                .readValue(content, BookResponseDtoWithoutCategoryIds[].class);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

    }
}
