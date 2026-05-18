package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.order.OrderItemResponseDto;
import org.project.onlinebookstore.dto.order.OrderRequestDto;
import org.project.onlinebookstore.dto.order.OrderResponseDto;
import org.project.onlinebookstore.dto.order.UpdateOrderStatusRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto requestDto);

    Page<OrderResponseDto> findAll(Pageable pageable);

    OrderResponseDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto requestDto);

    Page<OrderItemResponseDto> findItemsByOrderId(Long orderId, Pageable pageable);

    OrderItemResponseDto findItemByIdInOrder(Long orderId, Long itemId);
}
