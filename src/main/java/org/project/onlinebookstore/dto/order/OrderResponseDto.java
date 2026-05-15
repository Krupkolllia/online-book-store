package org.project.onlinebookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import org.project.onlinebookstore.model.order.OrderStatus;

public record OrderResponseDto(
        Long id,
        Long userId,
        Set<OrderItemResponseDto> orderItems,
        LocalDateTime orderDate,
        BigDecimal total,
        OrderStatus status
) {}
