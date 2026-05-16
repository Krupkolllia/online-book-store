package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.order.OrderItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderItemService {
    Page<OrderItemResponseDto> findItemsByOrderId(Long orderId, Pageable pageable);

    OrderItemResponseDto findItemByIdInOrder(Long orderId, Long itemId);
}
