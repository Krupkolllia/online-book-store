package org.project.onlinebookstore.repository.order;

import java.util.Optional;
import org.project.onlinebookstore.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    @NonNull
    @EntityGraph(attributePaths = {"orderItems", "orderItems.book"})
    Page<Order> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"orderItems", "orderItems.book"})
    Optional<Order> findById(@NonNull Long id);
}
