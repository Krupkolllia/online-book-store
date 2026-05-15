package org.project.onlinebookstore.repository.cart;

import java.util.Optional;
import org.project.onlinebookstore.model.cart.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph(attributePaths = {"book"})
    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long cartId);

    boolean existsByIdAndShoppingCartId(Long id, Long cartId);
}
