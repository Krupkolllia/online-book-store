package org.project.onlinebookstore.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.onlinebookstore.model.cart.ShoppingCart;
import org.project.onlinebookstore.repository.cart.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.project.onlinebookstore.util.TestDataHelper.ADD_SCRIPT_PATH;
import static org.project.onlinebookstore.util.TestDataHelper.DELETE_SCRIPT_PATH;
import static org.project.onlinebookstore.util.TestDataHelper.createShoppingCart;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @Sql(scripts = ADD_SCRIPT_PATH, executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_SCRIPT_PATH, executionPhase = AFTER_TEST_METHOD)
    @DisplayName("""
            findById method with id of existing ShoppingCart
            should return Optional of that ShoppingCart
            """)
    public void findById_WithValidId_ShouldReturnOptionalOfShoppingCart() {
        // Given
        ShoppingCart expected = createShoppingCart();
        Long id = expected.getId();

        // When
        Optional<ShoppingCart> actual = shoppingCartRepository.findById(id);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual).get().isEqualTo(expected);
    }

    @Test
    @DisplayName("""
            findById method with id of non-existing ShoppingCart
            should return empty Optional of ShoppingCart
            """)
    public void findById_WithInvalidId_ShouldReturnEmptyOptionalOfShoppingCart() {
        // Given
        Long invalidId = 404L;

        // When
        Optional<ShoppingCart> actual = shoppingCartRepository.findById(invalidId);

        // Then
        assertThat(actual).isEmpty();
    }
}
