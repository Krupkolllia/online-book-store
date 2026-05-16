package org.project.onlinebookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.order.OrderItemResponseDto;
import org.project.onlinebookstore.exception.EntityNotFoundException;
import org.project.onlinebookstore.mapper.OrderItemMapper;
import org.project.onlinebookstore.model.order.OrderItem;
import org.project.onlinebookstore.model.user.User;
import org.project.onlinebookstore.repository.order.OrderItemRepository;
import org.project.onlinebookstore.security.SecurityUtil;
import org.project.onlinebookstore.service.OrderItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemResponseDto> findItemsByOrderId(Long orderId, Pageable pageable) {
        User user = SecurityUtil.getUserFromSecurityContext();
        Long userId = user.getId();

        return orderItemRepository.findAllByOrderIdAndOrderUserId(orderId, userId, pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemResponseDto findItemByIdInOrder(Long orderId, Long itemId) {
        User user = SecurityUtil.getUserFromSecurityContext();
        Long userId = user.getId();

        OrderItem orderItem = orderItemRepository
                .findByIdAndOrderIdAndOrderUserId(itemId, orderId, userId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "There is no order item with id: " + itemId)
                );

        return orderItemMapper.toDto(orderItem);
    }
}
