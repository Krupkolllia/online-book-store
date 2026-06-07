package org.project.onlinebookstore.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.onlinebookstore.model.cart.CartItem;
import org.project.onlinebookstore.model.cart.ShoppingCart;
import org.project.onlinebookstore.repository.cart.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.project.onlinebookstore.util.TestDataHelper.ADD_SCRIPT_PATH;
import static org.project.onlinebookstore.util.TestDataHelper.DELETE_SCRIPT_PATH;
import static org.project.onlinebookstore.util.TestDataHelper.createShoppingCart;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            findByIdAndShoppingCartId method with id of existing
            ShoppingCart and id of existing in ShoppingCart CartItem
            should return Optional of CartItem
            """)
    public void findByIdAndShoppingCartId_ValidCase_ShouldReturnOptionalOfCartItem() {
        // Given
        ShoppingCart shoppingCart = createShoppingCart();
        CartItem expected = List.copyOf(shoppingCart.getCartItems()).get(0);

        Long shoppingCartId = shoppingCart.getId();
        Long cartItemId = expected.getId();

        // When
        Optional<CartItem> actual = cartItemRepository.findByIdAndShoppingCartId(
                cartItemId, shoppingCartId
        );

        // Then
        assertThat(actual).isPresent();
        assertThat(actual).get().isEqualTo(expected);
    }

    @Test
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            findByIdAndShoppingCartId method with id of existing
            ShoppingCart and id of non-existing in ShoppingCart CartItem
            should return empty Optional
            """)
    public void findByIdAndShoppingCartId_WithInvalidCartItemId_ShouldReturnEmptyOptional() {
        // Given
        Long shoppingCartId = createShoppingCart().getId();
        Long invalidCartItemId = 404L;

        // When
        Optional<CartItem> actual = cartItemRepository.findByIdAndShoppingCartId(
                invalidCartItemId, shoppingCartId
        );

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            findByIdAndShoppingCartId method with id of non-existing
            ShoppingCart should return empty Optional
            """)
    public void findByIdAndShoppingCartId_WithInvalidShoppingCartId_ShouldReturnEmptyOptional() {
        // Given
        Long cartItemId = List.copyOf(createShoppingCart().getCartItems()).get(0).getId();
        Long invalidShoppingCartId = 404L;

        // When
        Optional<CartItem> actual = cartItemRepository.findByIdAndShoppingCartId(
                cartItemId, invalidShoppingCartId
        );

        // Then
        assertThat(actual).isEmpty();
    }
}
