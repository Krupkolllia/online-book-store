package org.project.onlinebookstore.repository.cart;

import java.util.Optional;
import org.project.onlinebookstore.model.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph(attributePaths = {"book"})
    Optional<CartItem> findByIdAndShoppingCartUserId(Long id, Long userId);

    boolean existsByIdAndShoppingCartUserId(Long id, Long userId);
}
