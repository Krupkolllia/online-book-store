package org.project.onlinebookstore.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.onlinebookstore.dto.cart.CartItemQuantityRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemResponseDto;
import org.project.onlinebookstore.dto.cart.ShoppingCartResponseDto;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.project.onlinebookstore.util.TestDataHelper.ADD_SCRIPT_PATH;
import static org.project.onlinebookstore.util.TestDataHelper.DELETE_SCRIPT_PATH;
import static org.project.onlinebookstore.util.TestDataHelper.USER_ID;
import static org.project.onlinebookstore.util.TestDataHelper.createCartItemResponseDtoList;
import static org.project.onlinebookstore.util.TestDataHelper.createShoppingCartResponseDto;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ShoppingCartControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails("test_user@mail.com")
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            addItemToCart when CartItem does not exists in ShoppingCart
            should add it to cart and return updated ShoppingCartResponseDto
            and 201 status code
            """)
    public void addItemToCart_AddNewItem_ShouldReturn201StatusCode() throws Exception {
        // Given
        Long bookId = 4L;
        int quantity = 5;

        CartItemRequestDto itemRequestDto = new CartItemRequestDto(
                bookId, quantity
        );

        List<CartItemResponseDto> cartItemResponseDtoList = createCartItemResponseDtoList();

        CartItemResponseDto expectedAddedCartItem = new CartItemResponseDto(
                null, bookId, "Test title 4", quantity
        );
        cartItemResponseDtoList.add(expectedAddedCartItem);

        ShoppingCartResponseDto expected = new ShoppingCartResponseDto(
                USER_ID, USER_ID, cartItemResponseDtoList
        );

        String jsonRequest = objectMapper.writeValueAsString(itemRequestDto);

        // When
        MvcResult result = mockMvc.perform(
                post("/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);

        assertThat(actual.cartItems()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsExactlyInAnyOrderElementsOf(expected.cartItems());

        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("cartItems")
                .isEqualTo(expected);
    }

    @Test
    @WithUserDetails("test_user@mail.com")
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            addItemToCart method when CartItem does exist in ShoppingCart
            should add current quantity with requested in CartItemRequestDto
            and return updated ShoppingCartResponseDto with status code 201
            """)
    public void addItemToCart_AddExistingItem_ShouldReturn201StatusCode() throws Exception {
        // Given
        Long bookId = 1L;
        int quantity = 88;

        CartItemRequestDto itemRequestDto = new CartItemRequestDto(
                bookId, quantity
        );

        List<CartItemResponseDto> cartItemResponseDtoList = createCartItemResponseDtoList();
        int oldQuantity = cartItemResponseDtoList.get(0).quantity();
        cartItemResponseDtoList.set(0, new CartItemResponseDto(
                10L, bookId, "Test title 1", oldQuantity + quantity
        ));

        ShoppingCartResponseDto expected = new ShoppingCartResponseDto(
                USER_ID, USER_ID, cartItemResponseDtoList
        );

        String jsonRequest = objectMapper.writeValueAsString(itemRequestDto);

        // When
        MvcResult result = mockMvc.perform(
                post("/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithUserDetails("test_user@mail.com")
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            findCart method should return ShoppingCart for User
            in SecurityContext in format of response with
            ShoppingCartResponseDto and status code 200
            """)
    public void findCart_ValidCase_ShouldReturnStatusCode200() throws Exception {
        // Given
        ShoppingCartResponseDto expected = createShoppingCartResponseDto();

        // When
        MvcResult result = mockMvc.perform(
                get("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        // Then
        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithUserDetails("test_user@mail.com")
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            updateCartItemQuantity with id of existing CartItem
            should update CartItem's quantity and return updated
            ShoppingCart in format of response with ShoppingCartResponseDto
            and status code 200
            """)
    public void updateCartItemQuantity_WithValidId_ShouldReturnStatusCode200() throws Exception {
        // Given
        Long cartItemId = 10L;
        int updatedQuantity = 77;

        CartItemQuantityRequestDto itemQuantityRequestDto = new CartItemQuantityRequestDto(
                updatedQuantity
        );

        List<CartItemResponseDto> cartItemResponseDtoList = createCartItemResponseDtoList();
        cartItemResponseDtoList.set(0,
                new CartItemResponseDto(10L, 1L, "Test title 1", updatedQuantity)
        );
        ShoppingCartResponseDto expected = new ShoppingCartResponseDto(
                USER_ID, USER_ID, cartItemResponseDtoList
        );

        String jsonRequest = objectMapper.writeValueAsString(itemQuantityRequestDto);

        // When
        MvcResult result = mockMvc.perform(
                put("/cart/items/{cartItemId}", cartItemId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        // Then
        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithUserDetails("test_user@mail.com")
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            updateCartItemQuantity method with id of non-existing
            CartItem should return response with status code 404
            """)
    public void updateCartItemQuantity_WithInvalidId_ShouldReturnStatusCode404() throws Exception {
        // Given
        Long invalidCartItemId = 404L;
        CartItemQuantityRequestDto itemQuantityRequestDto = new CartItemQuantityRequestDto(
                16
        );

        String jsonRequest = objectMapper.writeValueAsString(itemQuantityRequestDto);

        // When & Then
        mockMvc.perform(
                put("/cart/items/{cartItemId}", invalidCartItemId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("test_user@mail.com")
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            deleteCartItemFromCartById method with id for existing
            CartItem should delete CartItem by id and return response
            with status code 204
            """)
    public void deleteCartItemFromCartById_WithValidId_ShouldReturnStatusCode204() throws Exception {
        // Given
        Long cartItemId = 10L;

        // When
        mockMvc.perform(
                delete("/cart/items/{cartItemId}", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent());

        // Then
        MvcResult result = mockMvc.perform(
                get("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);

        assertThat(actual.cartItems()).hasSize(2);
    }

    @Test
    @WithUserDetails("test_user@mail.com")
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            deleteCartItemFromCartById method with id of non-existing
            CartItem should return response with status code 404
            """)
    public void deleteCartItemFromCartById_WithInvalidId_ShouldReturnStatusCode404() throws Exception {
        // Given
        Long invalidCartItemId = 404L;

        // When & Then
        mockMvc.perform(
                delete("/cart/items/{cartItemId}", invalidCartItemId)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }
}
