package org.project.onlinebookstore.repository.cart;

import jakarta.annotation.Nonnull;
import java.util.Optional;
import org.project.onlinebookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Override
    @NonNull
    @EntityGraph(attributePaths = {"cartItems", "cartItems.book"})
    Optional<ShoppingCart> findById(@Nonnull Long id);
}
