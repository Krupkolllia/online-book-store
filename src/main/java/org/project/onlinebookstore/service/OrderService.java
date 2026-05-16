package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.order.OrderRequestDto;
import org.project.onlinebookstore.dto.order.OrderResponseDto;
import org.project.onlinebookstore.dto.order.UpdateOrderStatusRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto requestDto);

    Page<OrderResponseDto> findAll(Pageable pageable);

    OrderResponseDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto requestDto);
}
